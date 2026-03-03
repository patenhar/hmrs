import { useState } from "react";
import { useGetAllPosts, useTriggerCelebrations } from "@/api/queries/usePost";
import PostCard from "@/components/Custom/PostCard";
import Can from "@/components/Custom/Can";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { PlusCircle, PartyPopper, X } from "lucide-react";
import { Outlet, useNavigate } from "react-router-dom";
import type { PostFilters, PostResDto } from "@/api/types/post.types";

const POST_FILTER_OPTIONS = [
  { id: "tag", label: "Tag", type: "text", placeholder: "e.g. Achievement" },
  { id: "from", label: "From", type: "date" },
  { id: "to", label: "To", type: "date" },
] as const;

type PostFilterId = (typeof POST_FILTER_OPTIONS)[number]["id"];
type ActiveFilter = { id: PostFilterId; value: string };

export default function SocialFeed() {
  const navigate = useNavigate();
  const [filters, setFilters] = useState<PostFilters>({});
  const [activeFilters, setActiveFilters] = useState<ActiveFilter[]>([]);

  const { isLoading, data } = useGetAllPosts(
    Object.keys(filters).length ? filters : undefined,
  );
  const posts = data?.data.data ?? [];

  const { mutate: triggerCelebrations, isPending: triggeringCelebrations } =
    useTriggerCelebrations();

  const addFilter = () => {
    const usedIds = new Set(activeFilters.map((f) => f.id));
    const next = POST_FILTER_OPTIONS.find((c) => !usedIds.has(c.id));
    if (!next) return;
    setActiveFilters([...activeFilters, { id: next.id, value: "" }]);
  };

  const removeFilter = (index: number) => {
    const updated = activeFilters.filter((_, i) => i !== index);
    setActiveFilters(updated);
    applyFilters(updated);
  };

  const updateFilter = (index: number, value: string) => {
    setActiveFilters(
      activeFilters.map((f, i) => (i === index ? { ...f, value } : f)),
    );
  };

  const changeFilterId = (index: number, newId: PostFilterId) => {
    setActiveFilters(
      activeFilters.map((f, i) => (i === index ? { id: newId, value: "" } : f)),
    );
  };

  const applyFilters = (filtersToApply = activeFilters) => {
    const built: PostFilters = {};
    for (const f of filtersToApply) {
      if (f.value.trim()) {
        if (f.id === "tag") built.tag = f.value.trim();
        if (f.id === "from") built.from = f.value;
        if (f.id === "to") built.to = f.value;
      }
    }
    setFilters(built);
  };

  const clearAll = () => {
    setActiveFilters([]);
    setFilters({});
  };

  const canAddMore = activeFilters.length < POST_FILTER_OPTIONS.length;

  return (
    <div className="p-8">
      <Outlet />

      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <h4 className="scroll-m-20 text-xl font-semibold tracking-tight">
          Posts
        </h4>
        <div className="flex items-center gap-2">
          <Can authority="MANAGE_ALL_POST">
            <Button
              variant="outline"
              onClick={() => triggerCelebrations()}
              disabled={triggeringCelebrations}
            >
              {triggeringCelebrations ? (
                <Spinner className="h-4 w-4 mr-1" />
              ) : (
                <PartyPopper className="h-4 w-4 mr-1" />
              )}
              Generate Celebrations
            </Button>
          </Can>
          <Can authority="ADD_POST" extraAuthority="MANAGE_ALL_POST">
            <Button onClick={() => navigate("add")}>Add Post</Button>
          </Can>
        </div>
      </div>

      {/* Filters */}
      <div className="flex flex-wrap items-center gap-2 mb-6">
        {activeFilters.map((filter, index) => {
          const col = POST_FILTER_OPTIONS.find((c) => c.id === filter.id);
          const usedElsewhere = new Set(
            activeFilters.filter((_, i) => i !== index).map((f) => f.id),
          );
          return (
            <div
              key={`${filter.id}-${index}`}
              className="flex items-center gap-1.5 rounded-md border px-2 py-1 text-sm"
            >
              <Select
                value={filter.id}
                onValueChange={(v) => changeFilterId(index, v as PostFilterId)}
              >
                <SelectTrigger
                  size="sm"
                  className="h-6 w-auto border-0 bg-transparent p-0 text-xs font-semibold shadow-none focus-visible:ring-0"
                >
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {POST_FILTER_OPTIONS.map((c) => (
                    <SelectItem
                      key={c.id}
                      value={c.id}
                      disabled={usedElsewhere.has(c.id)}
                    >
                      {c.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>

              <span className="text-muted-foreground text-xs">:</span>

              <Input
                type="text"
                value={filter.value}
                onChange={(e) => updateFilter(index, e.target.value)}
                placeholder={
                  col?.type === "date"
                    ? "YYYY-MM-DD"
                    : (col?.placeholder ?? col?.label)
                }
                className="h-6 w-28 border-0 bg-transparent p-0 text-xs shadow-none focus-visible:ring-0"
              />

              <button
                type="button"
                onClick={() => removeFilter(index)}
                aria-label={`Remove ${col?.label ?? filter.id} filter`}
                className="text-muted-foreground hover:text-foreground transition-colors"
              >
                <X className="h-3.5 w-3.5" />
              </button>
            </div>
          );
        })}

        {canAddMore && (
          <Button
            variant="outline"
            size="sm"
            className="h-8 gap-1.5 text-xs"
            onClick={addFilter}
          >
            <PlusCircle className="h-3.5 w-3.5" />
            Add Filter
          </Button>
        )}

        {activeFilters.length > 0 && (
          <Button
            size="sm"
            className="h-8 text-xs"
            onClick={() => applyFilters()}
          >
            Apply
          </Button>
        )}

        {Object.keys(filters).length > 0 && (
          <Button
            variant="ghost"
            size="sm"
            className="h-8 text-xs text-muted-foreground"
            onClick={clearAll}
          >
            Clear all
          </Button>
        )}
      </div>

      {/* Feed */}
      {isLoading ? (
        <div className="flex items-center justify-center h-64">
          <Spinner className="size-8" />
        </div>
      ) : posts.length === 0 ? (
        <div className="text-center py-12 text-gray-400">
          <p className="text-sm">
            No posts yet. Be the first to share an achievement!
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {posts.map((post: PostResDto) => (
            <PostCard key={post.pkPostId} post={post} />
          ))}
        </div>
      )}
    </div>
  );
}
