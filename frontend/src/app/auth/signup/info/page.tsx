import React from "react";

export default function InfoRoute() {
    return(
        <div className="flex flex-col justify-center py-12 sm:px-6 lg:px-8">
            <div className="mt-4 sm:mx-auto sm:w-full sm:max-w-md"></div>
                <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
                    <div className="mt-6">
                        <h1 className="mt-6 text-center text-3xl font-bold tracking-tight text-gray-900">
                         Let's get to know you!
                        </h1>
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
                </div>
            </div>
)}