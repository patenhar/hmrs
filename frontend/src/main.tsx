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
import ProfileInfo from "./pages/ProfileInfo.tsx";
import { UpdateJobForm } from "./components/Custom/UpdateJobForm.tsx";
import { GameBookingForm } from "./components/Custom/GameBookingForm.tsx";
import { AddGame } from "./components/Custom/AddGame.jsx";
import { UpdateGameForm } from "./components/Custom/UpdateGameForm.tsx";
import Game from "./pages/Game.tsx";
import SocialFeed from "./pages/SocialFeed.tsx";
import { AddPostForm } from "./components/Custom/AddPostForm.tsx";
import { EditPostForm } from "./components/Custom/EditPostForm.tsx";
import PostInfo from "./pages/PostInfo.tsx";
import { TooltipProvider } from "@/components/ui/tooltip";
import { AuthProvider } from "./context/AuthContext.tsx";
import ProtectedRoute from "./components/Custom/ProtectedRoute.tsx";
import { Unauthorized } from "./pages/Unauthorized.tsx";
import { NotFound } from "./pages/NotFound.tsx";
import { ErrorFallback } from "./pages/ErrorFallback.tsx";
import GameBooking from "./pages/GameBooking.tsx";
import Referral from "./pages/Referral.tsx";
import Users from "./pages/Users.tsx";
import RolesPermissions from "./pages/RolesPermissions.tsx";

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
            element: <ProtectedRoute required={"MANAGE_ALL_USER"} />,
            children: [
              {
                path: "register",
                element: <Register />,
              },
            ],
          },
          {
            element: <ProtectedRoute required={"MANAGE_ALL_USER"} />,
            children: [
              {
                path: "users",
                element: <Outlet />,
                children: [
                  {
                    index: true,
                    element: <Users />,
                  },
                  {
                    element: <ProtectedRoute required={"MANAGE_ALL_PROFILE"} />,
                    children: [
                      {
                        path: ":userId/profile/create",
                        element: <CreateProfile />,
                      },
                    ],
                  },
                ],
              },
            ],
          },
          {
            path: "roles-permissions",
            element: <ProtectedRoute required={"MANAGE_ALL_USER"} />,
            children: [
              {
                index: true,
                element: <RolesPermissions />,
              },
            ],
          },
          {
            element: (
              <ProtectedRoute
                required={["VIEW_PROFILE", "MANAGE_ALL_PROFILE"]}
              />
            ),
            children: [
              {
                path: "users/:userId/profile",
                element: <CreateProfile isEdit />,
              },
            ],
          },
          {
            element: (
              <ProtectedRoute
                required={["VIEW_ORGCHART", "MANAGE_ALL_ORGCHART"]}
              />
            ),
            children: [
              {
                path: "profile/:profileId/org-chart",
                element: <OrgChart />,
              },
            ],
          },
          {
            element: (
              <ProtectedRoute
                required={["VIEW_PROFILE", "MANAGE_ALL_PROFILE"]}
              />
            ),
            children: [
              {
                path: "profiles/:profileId",
                element: <ProfileInfo />,
              },
            ],
          },
          {
            path: "travels",
            element: (
              <ProtectedRoute required={["VIEW_TRAVEL", "MANAGE_ALL_TRAVEL"]} />
            ),
            children: [
              {
                index: true,
                element: <Travel />,
              },
              {
                element: <ProtectedRoute required={"MANAGE_ALL_TRAVEL"} />,
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
                    element: <ProtectedRoute required={"MANAGE_ALL_TRAVEL"} />,
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
                element: (
                  <ProtectedRoute
                    required={["VIEW_EXPENSE", "MANAGE_ALL_EXPENSE"]}
                  />
                ),
                children: [
                  {
                    index: true,
                    element: <Expense />,
                  },
                  {
                    element: (
                      <ProtectedRoute
                        required={["ADD_EXPENSE", "MANAGE_ALL_EXPENSE"]}
                      />
                    ),
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
                path: "users/:userTravelId/documents",
                element: (
                  <ProtectedRoute
                    required={["VIEW_DOCUMENT", "MANAGE_ALL_DOCUMENT"]}
                  />
                ),
                children: [
                  {
                    index: true,
                    element: <UserTravelDocuments />,
                  },
                  {
                    element: (
                      <ProtectedRoute
                        required={["ADD_DOCUMENT", "MANAGE_ALL_DOCUMENT"]}
                      />
                    ),
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
            element: (
              <ProtectedRoute required={["VIEW_JOB", "MANAGE_ALL_JOB"]} />
            ),
            children: [
              {
                index: true,
                element: <Job />,
              },
              {
                element: <ProtectedRoute required={"MANAGE_ALL_JOB"} />,
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
                    path: "update",
                    element: <UpdateJobForm />,
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
            path: "referrals",
            element: (
              <ProtectedRoute required={["REFER_JOB", "MANAGE_ALL_REFERRAL"]} />
            ),
            children: [
              {
                index: true,
                element: <Referral />,
              },
            ],
          },
          {
            path: "games",
            element: (
              <ProtectedRoute required={["VIEW_GAME", "MANAGE_ALL_GAME"]} />
            ),
            children: [
              {
                index: true,
                element: <Game />,
              },
              {
                element: <ProtectedRoute required={"MANAGE_ALL_GAME"} />,
                children: [
                  {
                    path: "add",
                    element: <AddGame />,
                  },
                ],
              },
              {
                path: ":gameId",
                element: <Outlet />,
                children: [
                  {
                    element: <ProtectedRoute required={"MANAGE_ALL_GAME"} />,
                    children: [
                      {
                        path: "update",
                        element: <UpdateGameForm />,
                      },
                    ],
                  },
                ],
              },
            ],
          },
          {
            path: "games/bookings",
            element: (
              <ProtectedRoute
                required={["VIEW_BOOKING", "MANAGE_ALL_BOOKING"]}
              />
            ),
            children: [
              {
                index: true,
                element: <GameBooking />,
              },
              {
                element: (
                  <ProtectedRoute
                    required={["ADD_BOOKING", "MANAGE_ALL_BOOKING"]}
                  />
                ),
                children: [
                  {
                    path: "add",
                    element: <GameBookingForm />,
                  },
                ],
              },
            ],
          },
          {
            path: "social/posts",
            element: (
              <ProtectedRoute required={["VIEW_POST", "MANAGE_ALL_POST"]} />
            ),
            children: [
              {
                index: true,
                element: <SocialFeed />,
              },
              {
                element: (
                  <ProtectedRoute required={["ADD_POST", "MANAGE_ALL_POST"]} />
                ),
                children: [
                  {
                    path: "add",
                    element: <AddPostForm />,
                  },
                ],
              },
              {
                path: ":postId",
                element: <PostInfo />,
              },
              {
                path: ":postId/edit",
                element: (
                  <ProtectedRoute
                    required={["MANAGE_POST", "MANAGE_ALL_POST"]}
                  />
                ),
                children: [
                  {
                    index: true,
                    element: <EditPostForm />,
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
