import { useGetOrgChart } from "@/api/queries/useOrgChart";

import { useParams } from "react-router-dom";
import { Tree, TreeNode } from "react-organizational-chart";
import { Children } from "react";
import { OrtChartCard } from "@/components/Custom/OrgChartCard";

export function OrgChart() {
  const { profileId } = useParams();
  const { isLoading, data } = useGetOrgChart(profileId);
  const managers = (data?.data.data.managers ?? []).reverse();
  const current = data?.data.data.profileResDto;
  const directReports = data?.data.data.directReports;
  console.log(data?.data.data);
  const tree = () => {
    let root = null;
    let prev = null;

    managers?.forEach((man) => {
      if (!current) return [];
      const node = { ...man, children: [] };
      if (!root) root = node;
      if (prev) {
        prev.children.push(node);
      }
      prev = node;
    });

    const curr = { ...current, children: [] };

    if (prev) {
      prev.children.push(curr);
    } else {
      root = curr;
    }

    directReports?.forEach((dr) => {
      curr.children.push({ ...dr, children: [] });
    });

    return root ? [root] : [];
  };
  const treeNodes = tree();

  const buildTree = (node) => (
    <TreeNode
      key={node.pkProfileId}
      label={
        // <div>asdf</div>
        <OrtChartCard
          title={node.name ?? ""}
          description={node.user?.email ?? ""}
          featured={""}
        />
      }
    >
      {node.children?.map((child) => buildTree(child))}
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
            <OrtChartCard title={"Roima"} description={""} featured={""} />
          }
        >
          {treeNodes.map((node) => buildTree(node))}
        </Tree>
      </div>
    </div>
  );
}
