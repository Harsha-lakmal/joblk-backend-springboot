import React from 'react';
import { Camera } from 'lucide-react';

const FacebookProfile = () => {
  return (
    <div className="max-w-3xl mx-auto bg-white">
      {/* Cover Photo */}
      <div className="relative h-64 bg-blue-100">
        <img 
          src="/api/placeholder/800/400" 
          alt="Cover" 
          className="w-full h-full object-cover"
        />
        <button className="absolute bottom-4 right-4 flex items-center gap-2 bg-white px-3 py-2 rounded-md">
          <Camera size={16} />
          <span>Edit cover photo</span>
        </button>
      </div>

      {/* Profile Section */}
      <div className="px-8 relative">
        {/* Profile Picture */}
        <div className="absolute -top-16">
          <div className="w-40 h-40 rounded-full border-4 border-white overflow-hidden">
            <img 
              src="/api/placeholder/160/160" 
              alt="Profile" 
              className="w-full h-full object-cover"
            />
          </div>
        </div>

        {/* Profile Info */}
        <div className="pt-24 pb-4">
          <div className="flex justify-between items-start">
            <div>
              <h1 className="text-3xl font-bold">Harsha Lakmal</h1>
              <p className="text-gray-600">4.4K friends</p>
              <div className="flex gap-1 mt-1">
                {[...Array(8)].map((_, i) => (
                  <div key={i} className="w-8 h-8 rounded-full bg-gray-200"/>
                ))}
              </div>
            </div>
            <div className="flex gap-2">
              <button className="bg-blue-500 text-white px-4 py-2 rounded-md">
                Add to story
              </button>
              <button className="bg-gray-200 px-4 py-2 rounded-md">
                Edit profile
              </button>
              <button className="bg-gray-200 px-3 py-2 rounded-md">
                •••
              </button>
            </div>
          </div>
        </div>

        {/* Navigation */}
        <div className="flex border-t">
          {['Posts', 'About', 'Friends', 'Photos', 'Videos', 'Reels', 'More'].map((item, i) => (
            <button 
              key={i}
              className={`px-4 py-3 font-medium ${
                i === 0 ? 'text-blue-500 border-b-2 border-blue-500' : 'text-gray-600'
              }`}
            >
              {item}
            </button>
          ))}
        </div>
      </div>

      {/* Intro Section */}
      <div className="grid grid-cols-12 gap-4 p-4">
        <div className="col-span-5">
          <div className="bg-gray-50 rounded-md p-4">
            <h2 className="font-bold text-xl mb-2">Intro</h2>
            <p className="text-gray-600 text-sm mb-4">
              Time has made us understand one thing well.it can make us important to someone but only for a while..
            </p>
            <button className="w-full bg-gray-200 py-2 rounded-md">
              Edit Bio
            </button>
            <div className="mt-4 space-y-2 text-sm">
              <p>Worked at Training Software Engineer</p>
              <p>Went to Thambuttegama Central College NC</p>
              <p>Lives in Galgamuwa, Sri Lanka</p>
              <p>From Kurunegala</p>
              <p>Single</p>
            </div>
          </div>
        </div>

        {/* Posts Section */}
        <div className="col-span-7">
          <div className="bg-white rounded-md shadow p-4 mb-4">
            <div className="flex gap-2">
              <div className="w-10 h-10 rounded-full bg-gray-200" />
              <input
                type="text"
                placeholder="What's on your mind?"
                className="bg-gray-100 rounded-full px-4 py-2 w-full"
              />
            </div>
            <div className="flex justify-between mt-4 pt-4 border-t">
              <button className="flex-1 flex justify-center items-center gap-2">
                <Camera className="text-red-500" />
                Live video
              </button>
              <button className="flex-1 flex justify-center items-center gap-2">
                <Camera className="text-green-500" />
                Photo/Video
              </button>
              <button className="flex-1 flex justify-center items-center gap-2">
                <Camera className="text-blue-500" />
                Life event
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default FacebookProfile;
