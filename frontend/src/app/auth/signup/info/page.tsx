import React from "react";

export default function InfoRoute() {
    return(
        <div className="mt-6">
            <label className="mt-6">
                Name
            </label>
            <div className="block text-sm font-medium text-gray-700">
                <input
                className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                required
                type="text"
                name="name"
                id="name"
                />
            </div>
            <label className="block text-sm font-medium text-gray-700" htmlFor="dob">
              Date of Birth
            </label>
            <div className="mt-1">
              <input
                className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                required
                type="date"
                name="dob"
                id="dob"
              />
            </div>
        </div>
        
        
)}