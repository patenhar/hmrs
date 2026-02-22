import { useDeletePost, useToggleLike } from "@/api/queries/usePost";
import { useAuth } from "@/context/AuthContext";
import type { PostResDto } from "@/api/types/post.types";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Heart, Pencil, Trash, Bot } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { formatDistanceToNow } from "date-fns";
import CommentSection from "./CommentSection";

interface Props {
  post: PostResDto;
}

export default function PostCard({ post }: Props) {
  const { user } = useAuth();
  const navigate = useNavigate();
  const { mutate: toggleLike, isPending: liking } = useToggleLike(
    post.pkPostId,
  );
  const { mutate: deletePost } = useDeletePost();

  const isOwner = user?.pkUserId === post.author?.pkUserId;
  const isHr = user?.role?.permissions?.some(
    (p) => p.permissionName === "MANAGE_POST",
  );

  return (
    <Card className="mb-4 shadow-sm">
      <CardHeader className="pb-2">
        <div className="flex items-start justify-between">
          <div className="flex items-center gap-2">
            {post.isSystemGenerated && (
              <Bot className="h-4 w-4 text-blue-500" title="System generated" />
            )}
            <div>
              <CardTitle className="text-base font-semibold">
                {post.title}
              </CardTitle>
              <p className="text-xs text-gray-500 mt-0.5">
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
          <div className="flex gap-1">
            {isOwner && !post.isSystemGenerated && (
              <Button
                variant="ghost"
                size="icon"
                className="h-8 w-8"
                onClick={() => navigate(`/social/posts/${post.pkPostId}/edit`)}
              >
                <Pencil className="h-4 w-4 text-gray-400" />
              </Button>
            )}
            {(isOwner || isHr) && (
              <Button
                variant="ghost"
                size="icon"
                className="h-8 w-8"
                onClick={() => {
                  const remarks =
                    isHr && !isOwner
                      ? (window.prompt("Reason for removal (required):") ?? "")
                      : undefined;
                  if (isHr && !isOwner && !remarks?.trim()) return;
                  deletePost({ id: post.pkPostId, remarks });
                }}
              >
                <Trash className="h-4 w-4 text-red-400" />
              </Button>
            )}
          </div>
        </div>
      </CardHeader>

      <CardContent className="pt-0">
        <p className="text-sm text-gray-700 whitespace-pre-wrap">
          {post.description}
        </p>

        {post.tags && post.tags.length > 0 && (
          <div className="flex flex-wrap gap-1 mt-3">
            {post.tags.map((tag) => (
              <Badge key={tag.pkTagId} variant="secondary" className="text-xs">
                {tag.tag}
              </Badge>
            ))}
          </div>
        )}

        {post.recentLikers && post.recentLikers.length > 0 && (
          <p className="text-xs text-gray-400 mt-2">
            Liked by{" "}
            {post.recentLikers
              .slice(0, 3)
              .map((u) => u.email)
              .join(", ")}
            {post.likeCount > 3 ? ` and ${post.likeCount - 3} others` : ""}
          </p>
        )}
      </CardContent>

      <CardFooter className="pt-0 flex flex-col items-start">
        <div className="flex items-center gap-2">
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
            <span className="text-xs">{post.likeCount}</span>
          </Button>
        </div>

        <CommentSection postId={post.pkPostId} comments={post.comments ?? []} />
      </CardFooter>
    </Card>
  );
}
