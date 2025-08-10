import * as React from 'react';
import { useEffect, useState } from 'react';
import Box from '@mui/material/Box';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider';
import type { Device } from '~/model/api.model';
import { Alert, Avatar, CircularProgress, ListItemAvatar, TextField } from '@mui/material';
import { fetchDevices } from '~/services/api.service';
import { Android, Apple } from '@mui/icons-material';

// DeviceList.tsx
type DeviceListProps = {
  selectedDevice: Device | null;
  setSelectedDevice: (device: Device) => void;
};

export function DeviceList({ selectedDevice, setSelectedDevice }: DeviceListProps) {
  const [query, setQuery] = useState<string | null>(null);
  const [devices, setDevices] = useState<Device[] | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    getDevices();
  }, []);

  useEffect(() => {
    const handler = setTimeout(() => {
      getDevices();
    }, 500); // debounce
    return () => clearTimeout(handler);
  }, [query]);

  async function getDevices() {
    setLoading(true);
    try {
      const res = await fetchDevices(query ?? '');
      setDevices(res);
    } finally {
      setLoading(false);
    }
  }

  function getDeviceAvatar(device: Device) {
    if (device.deviceInfo.osName.toLowerCase().includes('iOs')) {
      return <Apple />;
    } else {
      return <Android />;
    }
  }

  return (
    <Box
      sx={{
        width: '100%',
        maxWidth: 360,
        bgcolor: 'background.paper',
        display: 'flex',
        flexDirection: 'column',
        gap: 1,
        padding: 2,
      }}
    >
      <TextField
        label="Search devices"
        variant="outlined"
        value={query ?? ''}
        onChange={(e) => setQuery(e.target.value)}
      />
      <Divider />
      {loading && (
        <Box display={'flex'} justifyContent="center" alignItems="center" height={100}>
          <CircularProgress />
        </Box>
      )}
      {!loading && !devices?.length && <Alert severity={'warning'}>No devices found!</Alert>}
      <List>
        {!loading &&
          devices?.map((device, index) => (
            <ListItemButton
              key={device.pushToken}
              selected={device.pushToken === selectedDevice?.pushToken}
              onClick={() => setSelectedDevice(device)}
            >
              <ListItemAvatar>
                <Avatar>{getDeviceAvatar(device)}</Avatar>
              </ListItemAvatar>
              <ListItemText
                primary={device.deviceInfo.deviceName}
                secondary={`${device.deviceInfo.brand} ${device.deviceInfo.modelName}`}
              />
            </ListItemButton>
          ))}
      </List>
    </Box>
  );
}
