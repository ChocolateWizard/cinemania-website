import React, { useId } from "react";

export default function FileInputTest() {
    
  const id = useId();
  const [fileName, setFileName] = useState("");
  const placeholder = "Placeholder";
  const name = "file-input";

  const handleFileChange = (event) => {
    setFileName(event.target.files[0]?.name || "");
  };

  return (
    <div className="relative flex flex-col w-full">
      <label
        htmlFor={id}
        className="flex flex-row w-full h-10 bg-transparent outline outline-0 border
    rounded-[7px] border-blue-gray-200 hover:bg-blue-gray-200 hover:bg-opacity-20  hover:border-blue-gray-500 cursor-pointer "
      >
        <div
          className="flex items-center pl-[0.75rem] w-[90%] bg-transparent font-normal text-sm text-blue-gray-500
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
            className={`flex items-center w-[90%] bg-transparent  font-normal ${
              fileName ? "text-blue-gray-500 text-sm" : "hidden"
            }`}
          >
            {fileName}
          </div>
        </div>
        <div className="bg-[#d4d7d9] w-[10%] p-1 justify-center items-center  rounded-r-[6px]">
          <UploadSVG />
        </div>
      </label>
      <input
        id={id}
        name={name}
        type="file"
        onChange={handleFileChange}
        className="absolute w-1 h-1 opacity-0"
      />
      <p>{error}</p>
    </div>
  );
}
