import type { Route } from './+types/login';
import { Alert, Card, CardContent, CardHeader, TextField } from '@mui/material';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { useState } from 'react';
import { getActuatorInfo, setAuth } from '~/services/api.service';
import { useNavigate } from 'react-router';

export function meta({}: Route.MetaArgs) {
  return [{ title: 'Login' }];
}

export default function Login() {
  const navigate = useNavigate();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  async function tryLogin(e?: React.FormEvent) {
    if (e) e.preventDefault();
    setError('');
    setAuth(username, password);

    try {
      await getActuatorInfo();
      navigate('/');
    } catch (err) {
      setError('Login failed. Please check your credentials.');
      return;
    }
  }

  return (
    <Card sx={{ maxWidth: 500, margin: 'auto', mt: 5 }}>
      <CardHeader title={'Login'} />
      <CardContent>
        <Box display="flex" flexDirection="column" gap={3} component="form" onSubmit={tryLogin}>
          <TextField
            label="Username"
            variant="outlined"
            autoComplete="current-user"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            label="Password"
            variant="outlined"
            type="password"
            autoComplete="current-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <Button onClick={tryLogin} type="submit" variant="contained">
            Login
          </Button>

          {error && <Alert severity="error">{error}</Alert>}
        </Box>
      </CardContent>
    </Card>
  );
}
