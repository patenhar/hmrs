import { useState } from "react";
import { useDeletePost, useToggleLike } from "@/api/queries/usePost";
import { useAuth } from "@/context/AuthContext";
import { hasAuthority } from "@/utils/hasAuthority";
import type { PostResDto } from "@/api/types/post.types";
import { DeletePostDialog } from "@/components/Custom/DeletePostDialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Heart, Pencil, Trash, Bot, MessageCircle } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { formatDistanceToNow } from "date-fns";

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
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);

  const isOwner =
    !!user?.pkUserId &&
    !!post.author?.pkUserId &&
    user.pkUserId === post.author.pkUserId;
  const isHr = hasAuthority(user?.authorities ?? [], "MANAGE_ALL_POST");

  return (
    <>
      <Card
        className="shadow-sm cursor-pointer hover:shadow-md transition-shadow flex flex-col h-full"
        onClick={() => navigate(`/social/posts/${post.pkPostId}`)}
      >
        <CardHeader className="pb-2">
          <div className="flex items-start justify-between gap-2">
            <div className="flex items-start gap-2 min-w-0">
              {post.isSystemGenerated && (
                <Bot
                  className="h-4 w-4 text-blue-500 mt-0.5 shrink-0"
                  aria-label="System generated"
                />
              )}
              <CardTitle className="text-base font-semibold line-clamp-2 leading-snug">
                {post.title}
              </CardTitle>
            </div>
            <div
              className="flex gap-1 shrink-0"
              onClick={(e) => e.stopPropagation()}
            >
              {isOwner && !post.isSystemGenerated && (
                <Button
                  variant="ghost"
                  size="icon"
                  className="h-8 w-8"
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
                  className="h-8 w-8"
                  onClick={() => {
                    if (isHr && !isOwner) {
                      setDeleteDialogOpen(true);
                    } else {
                      deletePost({ id: post.pkPostId });
                    }
                  }}
                >
                  <Trash className="h-4 w-4 text-red-400" />
                </Button>
              )}
            </div>
          </div>
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
        </CardHeader>

        <CardContent className="pt-0 flex-1">
          <p className="text-sm text-gray-700 line-clamp-3">
            {post.description}
          </p>

          {post.tags && post.tags.length > 0 && (
            <div className="flex flex-wrap gap-1 mt-3">
              {post.tags.map((tag) => (
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
        </CardContent>

        <CardFooter
          className="pt-2 border-t mt-auto"
          onClick={(e) => e.stopPropagation()}
        >
          <div className="flex items-center gap-3 w-full">
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
            <div className="flex items-center gap-1 text-gray-500">
              <MessageCircle className="h-4 w-4" />
              <span className="text-xs">{post.commentCount}</span>
            </div>
          </div>
        </CardFooter>
      </Card>
      <DeletePostDialog
        postId={post.pkPostId}
        open={deleteDialogOpen}
        onOpenChange={setDeleteDialogOpen}
      />
    </>
  );
}
