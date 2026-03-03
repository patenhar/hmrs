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
    <Card className="border-muted mx-auto gap-2 w-fit">
      <CardHeader className="text-center">
        <CardTitle className="text-sm font-semibold">{title}</CardTitle>
      </CardHeader>

      {description && (
        <CardContent className="text-center text-xs text-muted-foreground">
          {featured && (
            <Badge className="mb-2 text-xs" variant="outline">
              {featured}
            </Badge>
          )}
          <CardDescription>{description}</CardDescription>
        </CardContent>
      )}
    </Card>
  );
}
