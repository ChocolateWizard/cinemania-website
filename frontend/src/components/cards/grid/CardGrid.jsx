import React from "react";

export default function CardGrid({ dataArray, CardComponent }) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5">
      {dataArray.map((data) => (
        <CardComponent data={data} key={data.id} />
      ))}
    </div>
  );
}
