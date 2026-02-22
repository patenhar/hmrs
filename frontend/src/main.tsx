import { createRoot } from "react-dom/client";
import { createBrowserRouter, Outlet, RouterProvider } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

import "./index.css";
import App from "./App.tsx";
import Home from "./pages/Home.tsx";
import Register from "./pages/Register.tsx";
import Login from "./pages/Login.tsx";
import { Job } from "./pages/Job.tsx";
import { AddJobForm } from "./components/Custom/AddJobForm.tsx";
import { ShareJobForm } from "./components/Custom/ShareJobForm.tsx";
import { ReferJobForm } from "./components/Custom/ReferJobForm.tsx";
import { ErrorBoundary } from "react-error-boundary";
import CreateProfile from "./pages/CreateProfile.tsx";
import { OrgChart } from "./pages/OrgChart.tsx";
import Travel from "./pages/Travel.tsx";
import TravelInfo from "./pages/TravelInfo.tsx";
import UserTravelDocuments from "./pages/UserTravelDocuments.tsx";
import Expense from "./pages/Expense.tsx";
import { UploadDocument } from "./components/Custom/UploadDocument.tsx";
import { AddTravelForm } from "./components/Custom/AddTravelForm.tsx";
import { UpdateTravelForm } from "./components/Custom/UpdateTravelForm.tsx";
import { AddExpenseForm } from "./components/Custom/AddExpenseForm.tsx";
import JobInfo from "./pages/JobInfo.tsx";
import { GameBookingForm } from "./components/Custom/GameBookingForm.tsx";
import Game from "./pages/Game.tsx";

const ErrorBoundaryLayout = () => (
  <ErrorBoundary FallbackComponent={ErrorFallback}>
    <Outlet />
  </ErrorBoundary>
);

const router = createBrowserRouter([
  {
    element: <ErrorBoundaryLayout />,
    children: [
      {
        path: "/",
        element: <App />,
        children: [
          {
            path: "",
            element: <Home />,
          },
          {
            path: "unauthorized",
            element: <Unauthorized />,
          },
          {
            path: "*",
            element: <NotFound />,
          },
          {
            path: "login",
            element: <Login />,
          },
          {
            element: <ProtectedRoute required={"ADD_USER"} />,
            children: [
              {
                path: "register",
                element: <Register />,
              },
            ],
          },
          {
            element: <ProtectedRoute required={"ADD_PROFILE"} />,
            children: [
              {
                path: "profile/create",
                element: <CreateProfile />,
              },
            ],
          },
          {
            element: <ProtectedRoute required={"VIEW_ORGCHART"} />,
            children: [
              {
                path: "profile/:profileId/org-chart",
                element: <OrgChart />,
              },
            ],
          },
          {
            path: "travels",
            element: <ProtectedRoute required={"VIEW_TRAVEL"} />,
            children: [
              {
                index: true,
                element: <Travel />,
              },
              {
                element: <ProtectedRoute required={"ADD_TRAVEL"} />,
                children: [
                  {
                    path: "add",
                    element: <AddTravelForm />,
                  },
                ],
              },
              {
                path: ":travelId",
                element: <Outlet />,
                children: [
                  {
                    index: true,
                    element: <TravelInfo />,
                  },
                  {
                    element: <ProtectedRoute required={"UPDATE_TRAVEL"} />,
                    children: [
                      {
                        path: "update",
                        element: <UpdateTravelForm />,
                      },
                    ],
                  },
                ],
              },
              {
                path: "users/:userTravelId/expenses",
                element: <ProtectedRoute required={"VIEW_EXPENSE"} />,
                children: [
                  {
                    index: true,
                    element: <Expense />,
                  },
                  {
                    element: <ProtectedRoute required={"ADD_EXPENSE"} />,
                    children: [
                      {
                        path: "add",
                        element: <AddExpenseForm />,
                      },
                    ],
                  },
                ],
              },
              {
                path: "documents",
                element: <ProtectedRoute required={"VIEW_DOCUMENT"} />,
                children: [
                  {
                    index: true,
                    element: <UserTravelDocuments />,
                  },
                  {
                    element: <ProtectedRoute required={"ADD_DOCUMENT"} />,
                    children: [
                      {
                        path: "upload",
                        element: <UploadDocument name={"travel"} />,
                      },
                    ],
                  },
                ],
              },
            ],
          },

          {
            path: "jobs",
            element: <ProtectedRoute required={"VIEW_JOB"} />,
            children: [
              {
                index: true,
                element: <Job />,
              },
              {
                element: <ProtectedRoute required={"ADD_JOB"} />,
                children: [
                  {
                    path: "add",
                    element: <AddJobForm />,
                  },
                ],
              },
              {
                path: ":jobId",
                element: <Outlet />,
                children: [
                  {
                    index: true,
                    element: <JobInfo />,
                  },
                  {
                    path: "share",
                    element: <ShareJobForm />,
                  },
                  {
                    path: "refer",
                    element: <ReferJobForm />,
                  },
                ],
              },
            ],
          },
          {
            path: "games/bookings",
            element: <ProtectedRoute required={"VIEW_BOOKINGS"} />,
            children: [
              {
                index: true,
                element: <Game />,
              },
              {
                element: <ProtectedRoute required={"ADD_BOOKING"} />,
                children: [
                  {
                    path: "add",
                    element: <GameBookingForm />,
                  },
                ],
              },
            ],
          },
        ],
      },
    ],
  },
]);

import { TooltipProvider } from "@/components/ui/tooltip";
import { AuthProvider } from "./context/AuthContext.tsx";
import ProtectedRoute from "./components/Custom/ProtectedRoute.tsx";
import { Unauthorized } from "./pages/Unauthorized.tsx";
import { NotFound } from "./pages/NotFound.tsx";
import { ErrorFallback } from "./pages/ErrorFallback.tsx";

const queryClient = new QueryClient();

createRoot(document.getElementById("root")!).render(
  <QueryClientProvider client={queryClient}>
    <AuthProvider>
      <TooltipProvider>
        <RouterProvider router={router} />
      </TooltipProvider>
    </AuthProvider>
  </QueryClientProvider>,
);
