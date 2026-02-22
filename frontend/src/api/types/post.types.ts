export interface TagResDto {
  pkTagId: string;
  tag: string;
}

export interface PostVisibilityResDto {
  pkPostVisibilityId: string;
  visibility: string;
}

export interface UserDto {
  pkUserId: string;
  email: string;
  role?: {
    pkRoleId: string;
    roleName: string;
    permissions: { pkPermissionId: string; permissionName: string }[];
  };
}

export interface CommentResDto {
  pkCommentId: string;
  content: string;
  author: UserDto | null;
  createdAt: string;
  updatedAt: string;
  isDeleted: boolean;
  replies: CommentResDto[];
}

export interface PostResDto {
  pkPostId: string;
  title: string;
  description: string;
  isDeleted: boolean;
  isSystemGenerated: boolean;
  likeCount: number;
  commentCount: number;
  likedByCurrentUser: boolean;
  recentLikers: UserDto[];
  tags: TagResDto[];
  visibility: PostVisibilityResDto | null;
  author: UserDto | null;
  createdAt: string;
  updatedAt: string;
  comments: CommentResDto[];
}

export interface PostReqDto {
  title: string;
  description: string;
  tagIds: string[];
  visibilityId: string;
}

export interface CommentReqDto {
  postId: string;
  parentCommentId?: string;
  content: string;
}

export interface PostFilters {
  authorId?: string;
  tag?: string;
  from?: string;
  to?: string;
}
