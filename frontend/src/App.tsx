import { Outlet } from "react-router-dom";
import { Toaster } from "@/components/ui/sonner";
import Navbar from "./components/Custom/Navbar";

export function App() {
  return (
    <div>
      <Navbar />
      <Outlet />
      <Toaster />
    </div>
  );
}

export default App;
