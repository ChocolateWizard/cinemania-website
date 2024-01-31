import React, { useState, useEffect } from "react";
import { fetchCountries, register } from "../../utils/Api";
import CardLoader from "../helpers/loaders/cardLoader/CardLoader";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [countries, setCountries] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userJsonValues, setUserJsonValues] = useState({
    first_name: "",
    last_name: "",
    gender: "male",
    profile_name: "",
    username: "",
    email: "",
    password: "",
  });
  const [profileImage, setProfileImage] = useState(null);
  const [role, setRole] = useState("REGULAR");
  const [countryId, setCountryId] = useState(null);

  useEffect(() => {
    setLoading(true);

    fetchCountries()
      .then((response) => {
        if (response.status == 200) {
          setCountries(response.data);
          setCountryId("" + response.data[0].id);
        } else {
          console.error("Unable to retreive countries: " + response.data);
          setError(response.data);
        }
      })
      .catch((err) => {
        console.error("Unable to retreive countries: " + err);
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  const handleChange = (e) => {
    if (e.target.type === "file") {
      setProfileImage(e.target.files[0]);
    } else {
      setUserJsonValues({
        ...userJsonValues,
        [e.target.name]: e.target.value,
      });
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append(
      "user",
      new Blob(
        [
          JSON.stringify({
            first_name: userJsonValues.first_name,
            last_name: userJsonValues.last_name,
            gender: userJsonValues.gender,
            profile_name: userJsonValues.profile_name,
            username: userJsonValues.username,
            email: userJsonValues.email,
            password: userJsonValues.password,
            role: role,
            country_id: parseInt(countryId),
          }),
        ],
        { type: "application/json" }
      )
    );

    if (profileImage != null) {
      formData.append("profile_image", profileImage);
    }
    register(formData)
      .then((response) => {
        if (response.status == 200) {
          toast.success("Successful registration!");
          navigate('/login');
        } else {
          console.error(response.data);
          toast.error("Unable to register!");
        }
      })
      .catch((err) => {       
        console.error(err);
        toast.error("Unable to register!");
      });
  };
  //============================================================================================

  if (loading) {
    return <CardLoader />;
  }
  if (error != null) {
    return (
      <h2 className="px-4 mt-5 uppercase tracking-wider text-onyx-primary-30 text-lg font-bold">
        Unable to load register form
      </h2>
    );
  }

  return (
    <section>
      <div className="flex flex-col items-center justify-center px-6 my-4 mx-autos">
        <div className="w-full bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
          <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
            <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
              Create and account
            </h1>
            <form className="space-y-4 md:space-y-6" onSubmit={handleSubmit}>
              {/* first and last name */}
              <div className="flex flex-row justify-between">
                <div>
                  <label
                    htmlFor="first_name"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    First name
                  </label>
                  <input
                    type="text"
                    name="first_name"
                    id="first_name"
                    className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="First name"
                    required=""
                    onChange={handleChange}
                  />
                </div>
                <div>
                  <label
                    htmlFor="last_name"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Last name
                  </label>
                  <input
                    type="text"
                    name="last_name"
                    id="last_name"
                    className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="Last name"
                    required=""
                    onChange={handleChange}
                  />
                </div>
              </div>
              {/* gender */}
              <div>
                <label className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
                  Gender
                </label>
                <div className="flex flex-row ">
                  <div className="">
                    <input
                      type="radio"
                      name="gender"
                      id="gender_male"
                      className=""
                      value="male"
                      onChange={handleChange}
                      defaultChecked={userJsonValues.gender === "male"}
                    />
                    <label
                      htmlFor="gender_male"
                      className="pl-1 text-gray-900 dark:text-white"
                    >
                      Male
                    </label>
                  </div>
                  <div className="pl-7">
                    <input
                      type="radio"
                      name="gender"
                      id="gender_female"
                      className=""
                      value="female"
                      onChange={handleChange}
                    />
                    <label
                      htmlFor="gender_female"
                      className="pl-1 text-gray-900 dark:text-white"
                    >
                      Female
                    </label>
                  </div>
                  <div className="pl-7">
                    <input
                      type="radio"
                      name="gender"
                      id="gender_other"
                      className=""
                      value="other"
                      onChange={handleChange}
                    />
                    <label
                      htmlFor="gender_other"
                      className="pl-1 text-gray-900 dark:text-white"
                    >
                      Other
                    </label>
                  </div>
                </div>
              </div>
              {/* role and country */}
              <div className="flex flex-row justify-between">
                <div>
                  <label
                    htmlFor="role"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Role
                  </label>
                  <select
                    name="role"
                    id="role"
                    value={role}
                    required=""
                    className="block w-50 p-2.5 bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600  dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    onChange={(e) => {
                      setRole(e.target.value);
                    }}
                  >
                    <option value="REGULAR">Regular</option>
                    <option value="CRITIC">Critic</option>
                  </select>
                </div>
                <div className="pl-5">
                  <label
                    htmlFor="country_id"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Country
                  </label>
                  <select
                    name="country_id"
                    id="country_id"
                    required=""
                    className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    onChange={(e) => {
                      setCountryId("" + e.target.value);
                    }}
                  >
                    {countries.map((country, index) => {
                      return (
                        <option key={index} value={country.id}>
                          {country.name}
                        </option>
                      );
                    })}
                  </select>
                </div>
              </div>
              {/* profile name */}
              <div>
                <label
                  htmlFor="profile_name"
                  className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                >
                  Profile name
                </label>
                <input
                  type="text"
                  name="profile_name"
                  id="profile_name"
                  className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  placeholder="Profile name"
                  required=""
                  onChange={handleChange}
                />
              </div>
              {/* profile image */}
              <div>
                <label
                  htmlFor="profile_image"
                  className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                >
                  Profile image
                </label>
                <input
                  type="file"
                  name="profile_image"
                  id="profile_image"
                  className="text-gray-900 mt-2 "
                  accept=".png, .jpg, .jpeg"
                  onChange={handleChange}
                />
              </div>
              {/* email */}
              <div>
                <label
                  htmlFor="email"
                  className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                >
                  Email
                </label>
                <input
                  type="email"
                  name="email"
                  id="email"
                  className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  placeholder="name@company.com"
                  required=""
                  onChange={handleChange}
                />
              </div>
              {/* username */}
              <div>
                <label
                  htmlFor="username"
                  className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                >
                  Username
                </label>
                <input
                  type="text"
                  name="username"
                  id="username"
                  className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  placeholder="Username"
                  required=""
                  onChange={handleChange}
                />
              </div>
              {/* password */}
              <div>
                <label
                  htmlFor="password"
                  className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                >
                  Password
                </label>
                <input
                  type="password"
                  name="password"
                  id="password"
                  placeholder="••••••••"
                  className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  required=""
                  onChange={handleChange}
                />
              </div>
              <button
                type="submit"
                className="w-full text-white bg-primary-600 hover:bg-primary-700 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800"
              >
                Create an account
              </button>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
}
