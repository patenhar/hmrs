import { useGetOrgChart } from "@/api/queries/useOrgChart";
import { useParams } from "react-router-dom";
import { Tree, TreeNode } from "react-organizational-chart";
import { OrtChartCard } from "@/components/Custom/OrgChartCard";
import { Spinner } from "@/components/ui/spinner";

export function OrgChart() {
  const { profileId } = useParams();
  const { isLoading, data, isError } = useGetOrgChart(profileId!);

  if (isLoading) {
    return (
      <div className="flex justify-center items-center py-20">
        <Spinner className="size-8" />
      </div>
    );
  }

  if (isError || !data?.data?.data) {
    return (
      <div className="flex justify-center items-center py-20 text-muted-foreground">
        Failed to load org chart.
      </div>
    );
  }

  type ProfileNode = {
    pkProfileId?: string;
    name?: string;
    user?: { email?: string };
    children: ProfileNode[];
  };

  const managers = [...(data.data.data.managers ?? [])].reverse();
  const current = data.data.data.profileResDto;
  const directReports = data.data.data.directReports ?? [];

  if (!current) {
    return (
      <div className="flex justify-center items-center py-20 text-muted-foreground">
        No profile found.
      </div>
    );
  }

  const tree = (): ProfileNode[] => {
    const curr: ProfileNode = {
      ...current,
      children: directReports.map((dr: ProfileNode) => ({
        ...dr,
        children: [],
      })),
    };

    if (managers.length === 0) return [curr];

    const nodes: ProfileNode[] = managers.map((man) => ({
      ...(man as ProfileNode),
      children: [],
    }));

    for (let i = 0; i < nodes.length - 1; i++) {
      nodes[i].children = [nodes[i + 1]];
    }

    nodes.at(-1)!.children = [curr];

    return [nodes[0]];
  };

  const treeNodes = tree();

  const buildTree = (node: ProfileNode) => (
    <TreeNode
      key={node.pkProfileId}
      label={
        <OrtChartCard
          title={node.name ?? ""}
          description={node.user?.email ?? ""}
          featured={node.user?.role?.roleName ?? ""}
        />
      }
    >
      {node.children?.map((child: ProfileNode) => buildTree(child))}
    </TreeNode>
  );

  return (
    <div className="w-full overflow-x-auto">
      <div className="flex justify-center min-w-max py-10">
        <Tree
          lineWidth="3px"
          lineColor="blue"
          lineBorderRadius="10px"
          label={
            <OrtChartCard
              title={"Roima"}
              description={"contact@roimaint.com"}
              featured={"Company"}
            />
          }
        >
          {treeNodes.map((node) => buildTree(node))}
        </Tree>
      </div>
    </div>
  );
}
