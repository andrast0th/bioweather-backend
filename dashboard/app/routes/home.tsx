import Devices from "~/components/devices/devices";
import type {Route} from "./+types/home";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "New React Router App" },
    { name: "description", content: "Login to React Router!" },
  ];
}

export default function Home() {
  return <Devices />;
}
