import { useState } from "react";
import { useGetAllPosts } from "@/api/queries/usePost";
import { useGetAllTags } from "@/api/queries/useTag";
import PostCard from "@/components/Custom/PostCard";
import Can from "@/components/Custom/Can";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { Input } from "@/components/ui/input";
import { Outlet, useNavigate } from "react-router-dom";
import type { PostFilters, TagResDto } from "@/api/types/post.types";

export default function SocialFeed() {
  const navigate = useNavigate();
  const [filters, setFilters] = useState<PostFilters>({});
  const [tagInput, setTagInput] = useState("");
  const [fromDate, setFromDate] = useState("");
  const [toDate, setToDate] = useState("");

  const { isLoading, data } = useGetAllPosts(
    Object.keys(filters).length ? filters : undefined,
  );
  const { data: tagsData } = useGetAllTags();
  const tags: TagResDto[] = tagsData?.data.data ?? [];

  const posts = data?.data.data ?? [];

  const applyFilters = () => {
    const newFilters: PostFilters = {};
    if (tagInput.trim()) newFilters.tag = tagInput.trim();
    if (fromDate) newFilters.from = fromDate;
    if (toDate) newFilters.to = toDate;
    setFilters(newFilters);
  };

  const clearFilters = () => {
    setFilters({});
    setTagInput("");
    setFromDate("");
    setToDate("");
  };

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <Outlet />

      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
          Achievements Feed
        </h4>
        <Can authority="ADD_POST">
          <Button onClick={() => navigate("add")}>New Post</Button>
        </Can>
      </div>

      {/* Filters */}
      <div className="flex flex-wrap gap-2 mb-6 p-3 bg-gray-50 rounded-md border">
        <div className="flex gap-1 items-center flex-wrap">
          <Input
            className="h-8 text-sm w-40"
            placeholder="Filter by tag..."
            value={tagInput}
            onChange={(e) => setTagInput(e.target.value)}
            list="tag-suggestions"
          />
          <datalist id="tag-suggestions">
            {tags.map((t) => (
              <option key={t.pkTagId} value={t.tag} />
            ))}
          </datalist>
          <Input
            className="h-8 text-sm w-36"
            type="date"
            value={fromDate}
            onChange={(e) => setFromDate(e.target.value)}
            placeholder="From"
          />
          <Input
            className="h-8 text-sm w-36"
            type="date"
            value={toDate}
            onChange={(e) => setToDate(e.target.value)}
            placeholder="To"
          />
          <Button size="sm" className="h-8" onClick={applyFilters}>
            Filter
          </Button>
          {Object.keys(filters).length > 0 && (
            <Button
              size="sm"
              variant="outline"
              className="h-8"
              onClick={clearFilters}
            >
              Clear
            </Button>
          )}
        </div>
      </div>

      {/* Feed */}
      {isLoading ? (
        <div className="flex justify-center py-12">
          <Spinner />
        </div>
      ) : posts.length === 0 ? (
        <div className="text-center py-12 text-gray-400">
          <p className="text-sm">
            No posts yet. Be the first to share an achievement!
          </p>
        </div>
      ) : (
        posts.map((post) => <PostCard key={post.pkPostId} post={post} />)
      )}
    </div>
  );
}
