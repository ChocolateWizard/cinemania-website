import React from "react";

export default function CritiqueCard({ critique, isUsersCritique }) {




  
  return (
    <div class="flex flex-col border-2 p-4">
      <div class="flex justify-between items-center">
        <div class="flex items-center">
          <img
            class="w-12 h-12 rounded-full"
            src={critique.critic.profile_image_url}
            alt="User Image"
          />
          <div class="ml-4">
            <p class="font-bold">{critique.critic.profile_name}</p>
            <div class="flex space-x-2 ">
              <button class="px-2 py-1 bg-red-500 text-white rounded">
                Remove
              </button>
              <button class="px-2 py-1 bg-blue-500 text-white rounded">
                Update
              </button>
            </div>
          </div>
        </div>
        <div class="text-2xl font-bold">{critique.rating}%</div>
      </div>
      <p class="mt-4 text-wrap break-all">{critique.description}</p>
    </div>
  );
}
