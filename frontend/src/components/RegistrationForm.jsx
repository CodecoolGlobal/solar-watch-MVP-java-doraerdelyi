import {useState} from "react";

function RegistrationForm({onRegister}) {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    async function handleSubmit(e) {
        e.preventDefault();
        const credentials = {email: email, password: password};
        onRegister(credentials);
    }

    return <form onSubmit={handleSubmit}>
        <fieldset className="fieldset bg-base-200 border-base-300 rounded-box w-xs border p-4">
            <legend className="fieldset-legend">Register</legend>

            <label className="label">Email</label>
            <input type="email"
                   value={email}
                   className="input"
                   placeholder="Email"
                   onChange={(e) => setEmail(e.target.value)}
                   required
            />

            <label className="label">Password</label>
            <input type="password"
                   value={password}
                   className="input"
                   placeholder="Password"
                   onChange={(e) => setPassword(e.target.value)}
                   required
            />
            <label className="fieldset-label">Password</label>
            <input
                type="password"
                placeholder="Confirm Password"
                className="input"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
            />
            <button type="submit" className="btn btn-neutral mt-4">Register</button>
        </fieldset>
    </form>
}

export default RegistrationForm;