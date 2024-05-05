import React, { useEffect, useState } from "react";
import CardLoader from "../helpers/loaders/cardLoader/CardLoader";
import TextInput from "../helpers/form/TextInput";
import Select from "../helpers/form/Select";
import { fetchCountries, register } from "../../utils/Api";
import FileInput from "../helpers/form/FileInput";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

//===========CONSTANTS=================================================================
const genders = [
  {
    value: "male",
    label: "Male",
  },
  {
    value: "female",
    label: "Female",
  },
  {
    value: "other",
    label: "Other",
  },
];
const roles = [
  {
    value: "REGULAR",
    label: "Regular",
  },
  {
    value: "CRITIC",
    label: "Critic",
  },
];
const profileNameRegex = /^[a-zA-Z0-9_]*$/;
const extraSpaceRegex = / +(?= )/g;
const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
const validExtensions = [".jpg", ".jpeg", ".png"];
const maxImageFileSize = 8388608;

//=====================================================================================
export default function RegisterPageTest() {
  const [loading, setLoading] = useState(true);
  const [countries, setCountries] = useState([]);

  useEffect(() => {
    fetchCountries()
      .then((res) =>
        setCountries(
          res.data.map((c) => {
            return { value: c.id, label: c.name };
          })
        )
      )
      .catch((err) => {
        console.error("Unable to retreive countries: " + err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <CardLoader />;
  }
  return <Form countries={countries} />;
}

function Form({ countries }) {
  const navigate = useNavigate();
  const [btnEnabled, setBtnEnabled] = useState(countries.length > 0);
  const [country, setCountry] = useState(null);
  const [gender, setGender] = useState(null);
  const [role, setRole] = useState(null);

  const [errors, setErrors] = useState({
    firstName: "",
    lastName: "",
    gender: "",
    role: "",
    country: countries.length > 0 ? "" : "Unable to load countries",
    profileName: "",
    profileImage: "",
    email: "",
    username: "",
    password: "",
    confirmPassword: "",
  });

  function handleSubmit(e) {
    e.preventDefault();
    setBtnEnabled(false);
    const formErrors = {
      firstName: "",
      lastName: "",
      gender: "",
      role: "",
      country: "",
      profileName: "",
      profileImage: "",
      email: "",
      username: "",
      password: "",
      confirmPassword: "",
    };
    const firstName = e.target.firstName.value
      .trim()
      .replace(extraSpaceRegex, "");
    const lastName = e.target.lastName.value
      .trim()
      .replace(extraSpaceRegex, "");
    const profileName = e.target.profileName.value.trim();
    const profileImage = e.target.profileImage.files[0];
    const email = e.target.email.value.trim();
    const username = e.target.username.value.trim();
    const password = e.target.password.value.trim();
    const confirmPassword = e.target.confirmPassword.value.trim();

    let isValid = true;

    //validate first name
    if (firstName.length == 0) {
      formErrors.firstName = "This field is mandatory";
      isValid = false;
    } else if (firstName.length >= 100) {
      formErrors.firstName = "First name must be less than 100 characters";
      isValid = false;
    }
    //validate last name
    if (lastName.length == 0) {
      formErrors.lastName = "This field is mandatory";
      isValid = false;
    } else if (lastName.length >= 100) {
      formErrors.lastName = "Last name must be less than 100 characters";
      isValid = false;
    }

    //validate gender
    if (gender == null) {
      formErrors.gender = "This field is mandatory";
      isValid = false;
    }
    //validate role
    if (role == null) {
      formErrors.role = "This field is mandatory";
      isValid = false;
    }
    //validate country
    if (country == null) {
      formErrors.country = "This field is mandatory";
      isValid = false;
    }
    //validate profile name
    if (profileName.length == 0) {
      formErrors.profileName = "This field is mandatory";
      isValid = false;
    } else if (profileName.length >= 100) {
      formErrors.profileName = "Profile name must be less than 100 characters";
      isValid = false;
    } else if (!profileNameRegex.test(profileName)) {
      formErrors.profileName =
        "Profile name must only contain letters, numbers and _";
      isValid = false;
    }

    //validate profile image
    if (profileImage) {
      const profileImageName = profileImage.name;
      if (
        !validExtensions.some((type) =>
          profileImageName.toLowerCase().endsWith(type)
        )
      ) {
        formErrors.profileImage =
          "Profile image must be of type .jpg, .jpeg or .png";
        isValid = false;
      } else if (profileImage.size > maxImageFileSize) {
        formErrors.profileImage = "Max allowed image size is 8 MB";
        isValid = false;
      }
    }

    //validate email
    if (email.length == 0) {
      formErrors.email = "This field is mandatory";
      isValid = false;
    } else if (email.length >= 300) {
      formErrors.email = "Email must be less than 300 characters";
      isValid = false;
    } else if (!emailRegex.test(email)) {
      formErrors.email = "Invalid email structure";
      isValid = false;
    }

    //validate username
    if (username.length == 0) {
      formErrors.username = "This field is mandatory";
      isValid = false;
    } else if (username.length >= 300) {
      formErrors.username = "Username must be less than 300 characters";
      isValid = false;
    }

    //validate password
    if (password.length == 0) {
      formErrors.password = "This field is mandatory";
      isValid = false;
    } else if (password.length < 6) {
      formErrors.password = "Password must be at least 6 characters";
      isValid = false;
    } else if (password.length >= 300) {
      formErrors.password = "Password must be less than 300 characters";
      isValid = false;
    }

    //confirm password
    if (!formErrors.password) {
      if (confirmPassword !== password) {
        formErrors.confirmPassword = "Password does not match";
        isValid = false;
      }
    }

    setErrors(formErrors);
    if (!isValid) {
      setBtnEnabled(true);
    } else {
      const formData = new FormData();
      formData.append(
        "user",
        new Blob(
          [
            JSON.stringify({
              first_name: firstName,
              last_name: lastName,
              gender: gender,
              profile_name: profileName,
              username: username,
              email: email,
              password: password,
              role: role,
              country_id: parseInt(country),
            }),
          ],
          { type: "application/json" }
        )
      );

      if (profileImage) {
        formData.append("profile_image", profileImage);
      }

      register(formData)
        .then((response) => {
          toast.success("Successful registration!");
          navigate("/login");
        })
        .catch((err) => {
          console.error(err);
          toast.error("Unable to register!");
        })
        .finally(() => {
          setBtnEnabled(true);
        });
    }
  }

  return (
    <section className="flex justify-center my-5 text-black">
      <div className="flex flex-col bg-white rounded-lg px-5 py-5">
        <h1 className="text-2xl font-bold tracking-tight text-gray-900">
          Sign up
        </h1>
        <h2 className="text-gray-500 mb-8 mt-1">
          Enter your details to register
        </h2>
        <form className="space-y-6" onSubmit={handleSubmit}>
          <div className="flex flex-row space-x-4">
            <TextInput
              className={"max-w-[200px]"}
              autoComplete="off"
              type={"text"}
              placeholder={"First name"}
              name={"firstName"}
              error={errors.firstName}
            />
            <TextInput
              className={"max-w-[200px]"}
              autoComplete="off"
              type={"text"}
              placeholder={"Last name"}
              name={"lastName"}
              error={errors.lastName}
            />
          </div>
          <div className="flex flex-row space-x-4">
            <Select
              placeholder={"Gender"}
              data={genders}
              disabled={false}
              error={errors.gender}
              setter={setGender}
            />
            <Select
              placeholder={"Role"}
              data={roles}
              disabled={false}
              error={errors.role}
              setter={setRole}
            />
          </div>
          <Select
            placeholder={"Country"}
            data={countries}
            disabled={countries.length === 0}
            error={errors.country}
            setter={setCountry}
          />
          <TextInput
            autoComplete="off"
            type={"text"}
            placeholder={"Profile name"}
            name={"profileName"}
            error={errors.profileName}
          />
          <FileInput
            name={"profileImage"}
            placeholder={"Profile image"}
            error={errors.profileImage}
          />

          <TextInput
            autoComplete="off"
            type={"email"}
            placeholder={"Email"}
            name={"email"}
            error={errors.email}
          />
          <TextInput
            autoComplete="off"
            type={"text"}
            placeholder={"Username"}
            name={"username"}
            error={errors.username}
          />
          <TextInput
            autoComplete="off"
            type={"password"}
            placeholder={"Password"}
            name={"password"}
            error={errors.password}
          />
          <TextInput
            autoComplete="off"
            type={"password"}
            placeholder={"Confirm password"}
            name={"confirmPassword"}
            error={errors.confirmPassword}
          />
          <button
            type="submit"
            disabled={!btnEnabled}
            className="w-full font-bold rounded-lg text-sm px-5 py-2.5 text-center text-white bg-primary-600 hover:bg-primary-700 focus:ring-primary-300 disabled:bg-primary-200"
          >
            {btnEnabled ? "Create an account" : "Signing up..."}
          </button>
        </form>
      </div>
    </section>
  );
}
