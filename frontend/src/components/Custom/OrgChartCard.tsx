import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

export function OrtChartCard({ title, description, featured }) {
  return (
    <Card className="w-56 border-muted">
      <CardHeader className="text-center pb-2">
        <CardTitle className="text-sm font-semibold">{title}</CardTitle>
        {featured && (
          <Badge className="mt-1 text-xs" variant="secondary">
            {featured}
          </Badge>
        )}
      </CardHeader>

      {description && (
        <CardContent className="text-center text-xs text-muted-foreground pt-0">
          <CardDescription>{description}</CardDescription>
        </CardContent>
      )}
    </Card>
  );
}
