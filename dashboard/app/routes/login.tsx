import type {Route} from "./+types/login";
import {Alert, Card, CardContent, CardHeader, TextField} from "@mui/material";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import {useState} from "react";
import {getActuatorInfo, setAuth} from "~/services/api.service";

export function meta({}: Route.MetaArgs) {
    return [
        {title: "Login"}
    ];
}

export default function Login() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    async function tryLogin() {
        setError('');
        setAuth(username, password);

        try {
            await getActuatorInfo();
        } catch (err) {
            setError('Login failed. Please check your credentials.');
            return;
        }
    }

    return (
        <Card sx={{maxWidth: 500, margin: "auto", mt: 5}}>
            <CardHeader title={'Login'}/>
            <CardContent>
                <Box display="flex" flexDirection="column" gap={3}>
                    <TextField label="Username"
                               variant="outlined"
                               autoComplete="current-user"
                               value={username}
                               onChange={e => setUsername(e.target.value)}/>
                    <TextField label="Password"
                               variant="outlined"
                               type="password"
                               autoComplete="current-password"
                               value={password}
                               onChange={e => setPassword(e.target.value)}/>

                    <Button onClick={tryLogin} variant="contained">Login</Button>

                    {error && <Alert severity="error">{error}</Alert>}
                </Box>
            </CardContent>
        </Card>
    );
}
