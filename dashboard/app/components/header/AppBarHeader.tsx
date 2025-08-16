import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import { Link } from 'react-router';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';

export default function AppBarHeader() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);

  const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const menuItems = [
    { label: 'Devices', to: '/' },
    { label: 'Caching', to: '/caching' },
    { label: 'Config', to: '/config' },
    { label: 'Translations', to: '/translations' },
  ];

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography
            variant="h6"
            component={Link}
            to="/"
            sx={{
              flexGrow: 1,
              padding: 2,
              textDecoration: 'none',
              color: 'inherit',
              cursor: 'pointer',
            }}
          >
            ☀️ BioWeather Dashboard
          </Typography>
          {isMobile ? (
            <>
              <IconButton
                size="large"
                edge="end"
                color="inherit"
                aria-label="menu"
                onClick={handleMenu}
              >
                <MenuIcon />
              </IconButton>
              <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleClose}>
                {menuItems.map((item) => (
                  <MenuItem key={item.to} component={Link} to={item.to} onClick={handleClose}>
                    {item.label}
                  </MenuItem>
                ))}
              </Menu>
            </>
          ) : (
            menuItems.map((item) => (
              <Button key={item.to} component={Link} to={item.to} color="inherit">
                {item.label}
              </Button>
            ))
          )}
        </Toolbar>
      </AppBar>
    </Box>
  );
}
