import { useState } from "react";
import { useAddComment, useDeleteComment } from "@/api/queries/useComment";
import { useAuth } from "@/context/AuthContext";
import type { CommentResDto } from "@/api/types/post.types";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { Spinner } from "@/components/ui/spinner";
import { MessageCircle, Trash, ChevronDown, ChevronUp } from "lucide-react";
import { formatDistanceToNow } from "date-fns";

interface Props {
  postId: string;
  comments: CommentResDto[];
}

function CommentItem({
  comment,
  postId,
  currentUserId,
}: {
  comment: CommentResDto;
  postId: string;
  currentUserId: string;
}) {
  const [showReplyInput, setShowReplyInput] = useState(false);
  const [replyText, setReplyText] = useState("");
  const { mutate: addComment, isPending: addingReply } = useAddComment(postId);
  const { mutate: deleteComment } = useDeleteComment(postId);

  const isOwner = comment.author?.pkUserId === currentUserId;

  const handleReply = () => {
    if (!replyText.trim()) return;
    addComment({
      postId,
      parentCommentId: comment.pkCommentId,
      content: replyText,
    });
    setReplyText("");
    setShowReplyInput(false);
  };

  return (
    <div className="pl-0 border-l-2 border-gray-100 mb-3">
      <div className="flex items-start gap-2">
        <div className="flex-1 bg-gray-50 rounded-md px-3 py-2">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-gray-800">
              {comment.author?.email ?? "Unknown"}
            </span>
            <span className="text-xs text-gray-400">
              {comment.createdAt
                ? formatDistanceToNow(new Date(comment.createdAt), {
                    addSuffix: true,
                  })
                : ""}
            </span>
          </div>
          <p className="text-sm text-gray-700 mt-1">{comment.content}</p>
        </div>
        <div className="flex gap-1 mt-1">
          <Button
            variant="ghost"
            size="sm"
            className="text-xs text-gray-500 h-7 px-2"
            onClick={() => setShowReplyInput(!showReplyInput)}
          >
            Reply
          </Button>
          {isOwner && (
            <Button
              variant="ghost"
              size="icon"
              className="h-7 w-7"
              onClick={() => deleteComment({ id: comment.pkCommentId })}
            >
              <Trash className="h-3 w-3 text-red-400" />
            </Button>
          )}
        </div>
      </div>

      {showReplyInput && (
        <div className="ml-4 mt-2 flex gap-2">
          <Textarea
            className="text-sm min-h-[60px]"
            placeholder="Write a reply..."
            value={replyText}
            onChange={(e) => setReplyText(e.target.value)}
          />
          <Button
            size="sm"
            onClick={handleReply}
            disabled={addingReply}
            className="self-end"
          >
            {addingReply ? <Spinner /> : "Reply"}
          </Button>
        </div>
      )}

      {comment.replies && comment.replies.length > 0 && (
        <div className="ml-6 mt-2 space-y-2">
          {comment.replies.map((reply) => (
            <div key={reply.pkCommentId} className="flex items-start gap-2">
              <div className="flex-1 bg-blue-50 rounded-md px-3 py-2">
                <div className="flex items-center justify-between">
                  <span className="text-sm font-medium text-gray-800">
                    {reply.author?.email ?? "Unknown"}
                  </span>
                  <span className="text-xs text-gray-400">
                    {reply.createdAt
                      ? formatDistanceToNow(new Date(reply.createdAt), {
                          addSuffix: true,
                        })
                      : ""}
                  </span>
                </div>
                <p className="text-sm text-gray-700 mt-1">{reply.content}</p>
              </div>
              {reply.author?.pkUserId === currentUserId && (
                <Button
                  variant="ghost"
                  size="icon"
                  className="h-7 w-7 mt-1"
                  onClick={() => deleteComment({ id: reply.pkCommentId })}
                >
                  <Trash className="h-3 w-3 text-red-400" />
                </Button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default function CommentSection({ postId, comments }: Props) {
  const { user } = useAuth();
  const [showComments, setShowComments] = useState(false);
  const [newComment, setNewComment] = useState("");
  const { mutate: addComment, isPending } = useAddComment(postId);

  const handleAddComment = () => {
    if (!newComment.trim()) return;
    addComment({ postId, content: newComment });
    setNewComment("");
  };

  return (
    <div className="mt-3">
      <Button
        variant="ghost"
        size="sm"
        className="text-gray-500 gap-1 h-8 px-2"
        onClick={() => setShowComments(!showComments)}
      >
        <MessageCircle className="h-4 w-4" />
        <span className="text-xs">
          {comments.length} comment{comments.length !== 1 ? "s" : ""}
        </span>
        {showComments ? (
          <ChevronUp className="h-3 w-3" />
        ) : (
          <ChevronDown className="h-3 w-3" />
        )}
      </Button>

      {showComments && (
        <div className="mt-3 space-y-2">
          {comments.map((comment) => (
            <CommentItem
              key={comment.pkCommentId}
              comment={comment}
              postId={postId}
              currentUserId={user?.pkUserId ?? ""}
            />
          ))}

          {user && (
            <div className="flex gap-2 mt-3">
              <Textarea
                className="text-sm min-h-[60px]"
                placeholder="Write a comment..."
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
              />
              <Button
                size="sm"
                onClick={handleAddComment}
                disabled={isPending}
                className="self-end"
              >
                {isPending ? <Spinner /> : "Post"}
              </Button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
