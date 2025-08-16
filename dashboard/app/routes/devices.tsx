import { DeviceList } from '~/components/devices/DeviceList';
import type { Route } from './+types/devices';
import type { Device } from '~/model/api.model';
import React, { useState } from 'react';
import { DevicePanel } from '~/components/devices/DevicePanel';
import Box from '@mui/material/Box';

export function meta({}: Route.MetaArgs) {
  return [{ title: 'BioWeather Dashboard - Devices' }];
}

export default function Devices() {
  const [device, setDevice] = useState<Device | null>(null);

  return (
    <Box>
      <DeviceList selectedDevice={device} setSelectedDevice={setDevice} />
      <DevicePanel device={device} />
    </Box>
  );
}
