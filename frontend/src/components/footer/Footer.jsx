import React from "react";

export default function Footer() {
  return (
    <footer className="flex flex-col justify-center items-center bg-onyx-tint sticky top-full h-[70px]">
      <div>Cinemania</div>
      <div>&copy;{new Date().getFullYear()}, Cinemania, Inc. and its affiliates</div>
    </footer>
  );
}
