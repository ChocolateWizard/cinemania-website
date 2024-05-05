import React, { useContext, useState } from "react";
import { login } from "../../utils/Api";
import { GlobalContext } from "../../context/GlobalState";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import CardLoader from "../helpers/loaders/cardLoader/CardLoader";
import TextInput from "../helpers/form/TextInput";

export default function LoginPage() {
  const { setSessionData } = useContext(GlobalContext);
  const navigate = useNavigate();

  const [logging, setLogging] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  function handleSubmit(e) {
    e.preventDefault();
    setLogging(true);

    login(
      JSON.stringify({
        username: username,
        password: password,
      })
    )
      .then((response) => {
        setSessionData(response.data);
        navigate(`/`);
      })
      .catch((err) => {
        console.error(err);
        setLogging(false);
        toast.error("Unable to login!");
      });
  }

  if (logging) {
    return <CardLoader />;
  }
  return (
    <section className="flex justify-center mt-20 text-black">
      <div className="flex flex-col w-[450px] bg-white rounded-lg px-5 py-5">
        <h1 className="text-2xl font-bold tracking-tight text-gray-900">
          Sign in
        </h1>
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <TextInput
            value={username}
            onChange={(e) => {
              setUsername(e.target.value);
            }}
            autoComplete="off"
            type={"text"}
            placeholder={"Username"}
            name={"username"}
          />
          <TextInput
            value={password}
            onChange={(e) => {
              setPassword(e.target.value);
            }}
            autoComplete="off"
            type={"password"}
            placeholder={"Password"}
            name={"password"}
          />
          <button
            type="submit"
            className="w-full font-bold rounded-lg text-sm px-5 py-2.5 text-center text-white bg-primary-600 hover:bg-primary-700 focus:ring-primary-300"
          >
            Login
          </button>
        </form>
      </div>
    </section>
  );
}
