import * as React from 'react';
import { useEffect, useState } from 'react';
import Box from '@mui/material/Box';
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';
import Avatar from '@mui/material/Avatar';
import CircularProgress from '@mui/material/CircularProgress';
import Alert from '@mui/material/Alert';
import { Android, Apple } from '@mui/icons-material';
import { useLocation, useNavigate } from 'react-router';
import type { Device } from '~/model/api.model';
import { fetchDevices } from '~/services/api.service';
import { Paper } from '@mui/material';

type DeviceListProps = {
  selectedDevice: Device | null;
  setSelectedDevice: (device: Device) => void;
};

export function DeviceList({ selectedDevice, setSelectedDevice }: DeviceListProps) {
  const [query, setQuery] = useState('');
  const [devices, setDevices] = useState<Device[]>([]);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    let active = true;
    setLoading(true);
    fetchDevices(query).then((result) => {
      if (active) {
        setDevices(result);
        setLoading(false);
      }
    });
    return () => {
      active = false;
    };
  }, [query]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const deviceToken = params.get('device');
    if (deviceToken && devices.length) {
      const found = devices.find((d) => d.pushToken === deviceToken);
      if (found && found.pushToken !== selectedDevice?.pushToken) {
        setSelectedDevice(found);
      }
    }
  }, [devices, location.search]);

  function handleChange(_: any, value: Device | null) {
    if (value) {
      const params = new URLSearchParams(location.search);
      params.set('device', value.pushToken);
      navigate(`${location.pathname}?${params.toString()}`, { replace: true });
      setSelectedDevice(value);
    }
  }

  function getDeviceAvatar(device: Device) {
    return device.deviceInfo.brand.toLowerCase() === 'apple' ? <Apple /> : <Android />;
  }

  return (
    <Paper elevation={3} sx={{ width: '100%', p: 2, justifyContent: 'center', display: 'flex' }}>
      <Autocomplete
        sx={{ width: 500 }}
        options={devices}
        loading={loading}
        value={selectedDevice}
        onChange={handleChange}
        getOptionLabel={(option) => option.deviceInfo.deviceName}
        isOptionEqualToValue={(option, value) => option.pushToken === value.pushToken}
        onInputChange={(_, value) => setQuery(value)}
        renderInput={(params) => (
          <TextField
            {...params}
            label="Select device"
            variant="outlined"
            InputProps={{
              ...params.InputProps,
              endAdornment: (
                <>
                  {loading ? <CircularProgress color="inherit" size={20} /> : null}
                  {params.InputProps.endAdornment}
                </>
              ),
            }}
          />
        )}
        renderOption={(props, option) => (
          <li {...props} key={option.pushToken}>
            <Avatar sx={{ mr: 1 }}>{getDeviceAvatar(option)}</Avatar>
            <Box>
              <div>{option.deviceInfo.deviceName}</div>
              <div style={{ fontSize: 12, color: '#888' }}>
                {option.deviceInfo.brand} {option.deviceInfo.modelName}
              </div>
            </Box>
          </li>
        )}
        noOptionsText={<Alert severity="warning">No devices found!</Alert>}
      />
    </Paper>
  );
}
