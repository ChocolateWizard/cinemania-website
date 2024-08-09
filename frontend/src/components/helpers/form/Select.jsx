import React from "react";
import { Select as LibSel, Option } from "@material-tailwind/react";

export default function Select({
  className,
  placeholder,
  data,
  error,
  disabled,
  setter,
}) {
  return (
    <div className={`flex flex-col w-full ${className}`}>
      <LibSel
        label={placeholder}
        disabled={disabled}
        error={disabled == false && error != ""}
        onChange={(val) => setter(val)}
      >
        {disabled ? (
          <Option value="a">a</Option>
        ) : (
          data.map((d, index) => {
            return (
              <Option  key={index} value={`${d.value}`}>
                {d.label}
              </Option>
            );
          })
        )}
      </LibSel>
      <p
        hidden={!error}
        className="mt-1 ml-1 text-wrap break-words overflow-hidden text-red-500 font-normal"
      >
        {error}
      </p>
    </div>
  );
}
