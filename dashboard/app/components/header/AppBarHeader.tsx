import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { Link } from 'react-router';

export default function AppBarHeader() {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1, padding: 2 }}>
            ☀️ BioWeather Dashboard
          </Typography>

          <Button component={Link} to="/">
            Devices
          </Button>
          <Button component={Link} to="/caching">
            Caching
          </Button>
          <Button component={Link} to="/translations">
            Translations
          </Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
