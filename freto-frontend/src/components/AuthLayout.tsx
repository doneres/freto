import type { ReactNode } from "react";

interface AuthLayoutProps {
  sidebar: ReactNode;
  children: ReactNode;
}

export function AuthLayout({ sidebar, children }: AuthLayoutProps) {
  return (
    <div className="flex h-screen w-screen overflow-hidden">
      <div className="hidden lg:flex lg:w-1/2 bg-[#7C3AED] flex-col justify-between p-10 overflow-hidden">
        {sidebar}
      </div>
      <div className="w-full lg:w-1/2 flex items-center justify-center bg-white px-8 overflow-y-auto">
        <div className="w-full max-w-md py-8">{children}</div>
      </div>
    </div>
  );
}
