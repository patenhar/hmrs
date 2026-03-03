import {
  useGetPostById,
  useDeletePost,
  useToggleLike,
} from "@/api/queries/usePost";
import { useAuth } from "@/context/AuthContext";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { Separator } from "@/components/ui/separator";
import { useNavigate, useParams } from "react-router-dom";
import { formatDistanceToNow } from "date-fns";
import { Heart, Pencil, Trash, Bot, ArrowLeft } from "lucide-react";
import CommentSection from "@/components/Custom/CommentSection";
import type { TagResDto, UserDto } from "@/api/types/post.types";

export default function PostInfo() {
  const { postId } = useParams<{ postId: string }>();
  const navigate = useNavigate();
  const { user } = useAuth();

  const { isLoading, data } = useGetPostById(postId ?? "");
  const post = data?.data.data;

  const { mutate: toggleLike, isPending: liking } = useToggleLike(postId ?? "");
  const { mutate: deletePost } = useDeletePost();

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spinner className="size-8" />
      </div>
    );
  }

  if (!post) {
    return (
      <div className="flex items-center justify-center h-64">
        <p className="text-gray-400">Post not found.</p>
      </div>
    );
  }

  const isOwner = user?.pkUserId === post.author?.pkUserId;
  const isHr = user?.role?.permissions?.some(
    (p) => p.permissionName === "MANAGE_POST",
  );

  return (
    <div className="p-8 max-w-3xl mx-auto">
      <Button
        variant="ghost"
        size="sm"
        className="mb-4 gap-1 text-gray-500"
        onClick={() => navigate(-1)}
      >
        <ArrowLeft className="h-4 w-4" />
        Back
      </Button>

      <Card className="shadow-sm">
        <CardHeader>
          <div className="flex items-start justify-between gap-2">
            <div className="flex items-start gap-2 min-w-0">
              {post.isSystemGenerated && (
                <Bot
                  className="h-5 w-5 text-blue-500 mt-0.5 shrink-0"
                  aria-label="System generated"
                />
              )}
              <div>
                <CardTitle className="text-xl font-semibold">
                  {post.title}
                </CardTitle>
                <p className="text-sm text-gray-500 mt-1">
                  {post.isSystemGenerated
                    ? "System"
                    : (post.author?.email ?? "Unknown")}
                  {" · "}
                  {post.createdAt
                    ? formatDistanceToNow(new Date(post.createdAt), {
                        addSuffix: true,
                      })
                    : ""}
                </p>
              </div>
            </div>
            <div className="flex gap-1 shrink-0">
              {isOwner && !post.isSystemGenerated && (
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={() =>
                    navigate(`/social/posts/${post.pkPostId}/edit`)
                  }
                >
                  <Pencil className="h-4 w-4 text-gray-400" />
                </Button>
              )}
              {(isOwner || isHr) && (
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => {
                    const remarks =
                      isHr && !isOwner
                        ? (window.prompt("Reason for removal (required):") ??
                          "")
                        : undefined;
                    if (isHr && !isOwner && !remarks?.trim()) return;
                    deletePost(
                      { id: post.pkPostId, remarks },
                      { onSuccess: () => navigate("/social/posts") },
                    );
                  }}
                >
                  <Trash className="h-4 w-4 text-red-400" />
                </Button>
              )}
            </div>
          </div>
        </CardHeader>

        <Separator />

        <CardContent className="pt-4">
          <p className="text-sm text-gray-800 whitespace-pre-wrap leading-relaxed">
            {post.description}
          </p>

          {post.tags && post.tags.length > 0 && (
            <div className="flex flex-wrap gap-1 mt-4">
              {post.tags.map((tag: TagResDto) => (
                <Badge
                  key={tag.pkTagId}
                  variant="secondary"
                  className="text-xs"
                >
                  #{tag.tag}
                </Badge>
              ))}
            </div>
          )}

          <div className="flex items-center gap-3 mt-4">
            <Button
              variant="ghost"
              size="sm"
              className={`gap-1 h-8 px-2 ${post.likedByCurrentUser ? "text-red-500" : "text-gray-500"}`}
              onClick={() => toggleLike()}
              disabled={liking}
            >
              <Heart
                className={`h-4 w-4 ${post.likedByCurrentUser ? "fill-red-500 text-red-500" : ""}`}
              />
              <span className="text-xs">{post.likeCount} likes</span>
            </Button>
          </div>

          {post.recentLikers && post.recentLikers.length > 0 && (
            <p className="text-xs text-gray-400 mt-1">
              Liked by{" "}
              {post.recentLikers
                .slice(0, 3)
                .map((u: UserDto) => u.email)
                .join(", ")}
              {post.likeCount > 3 ? ` and ${post.likeCount - 3} others` : ""}
            </p>
          )}
        </CardContent>

        <Separator />

        <CardContent className="pt-4">
          <CommentSection
            postId={post.pkPostId}
            comments={post.comments ?? []}
            defaultOpen
          />
        </CardContent>
      </Card>
    </div>
  );
}
