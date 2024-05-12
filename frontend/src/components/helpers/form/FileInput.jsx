import React, { useId, useState } from "react";
import UploadSVG from "../svg/UploadSVG";

export default function FileInput({ name, placeholder, error }) {
  const id = useId();
  const [fileName, setFileName] = useState("");

  const handleFileChange = (event) => {
    setFileName(event.target.files[0]?.name || "");
  };

  return (
    <div className="relative flex flex-col w-full min-w-[200px]">
      <label
        htmlFor={id}
        className="flex flex-row w-full h-10 bg-transparent outline outline-0 border  
    rounded-[7px] border-blue-gray-200 hover:bg-opacity-20 hover:border-2 transition-all hover:border-gray-900 cursor-pointer "
      >
        {/* FILE NAME PART */}
        <div
          className="flex items-center pl-[0.75rem] w-full bg-transparent font-normal text-sm text-blue-gray-500 
       transition-all  "
        >
          <div
            className={`absolute transition-all ease-in-out ${
              fileName
                ? "transform -translate-y-5 text-[11px] bg-white text-gray-900"
                : ""
            }`}
          >
            {placeholder}
          </div>
          <div
            className={`flex items-center bg-transparent font-normal ${
              fileName ? "text-blue-gray-500 text-sm" : "hidden"
            }`}
          >
            <div className="overflow-hidden whitespace-nowrap overflow-ellipsis max-w-[100px]">
              {fileName}
            </div>
          </div>
        </div>
        {/* SVG PART */}
        <div className="flex justify-center items-center bg-[#d4d7d9] w-[40px] rounded-r-[6px]">
          <UploadSVG width="30" height="30" className="stroke-white" />
        </div>
      </label>
      <input
        id={id}
        name={name}
        type="file"
        onChange={handleFileChange}
        className="absolute w-1 h-1 opacity-0"
      />
      <p
        hidden={!error}
        className="mt-1 ml-1 text-wrap break-words overflow-hidden text-red-500 font-normal"
      >
        {error}
      </p>
    </div>
  );
}
