import { DeviceList } from '~/components/devices/DeviceList';
import type { Route } from './+types/home';
import { Grid } from '@mui/material';
import type { Device } from '~/model/api.model';
import React, { useState } from 'react';
import { DevicePanel } from '~/components/devices/DevicePanel';

export function meta({}: Route.MetaArgs) {
  return [{ title: 'BioWeather Dashboard - Devices' }];
}

export default function Home() {
  const [device, setDevice] = useState<Device | null>(null);

  return (
    <Grid container spacing={1}>
      <Grid size={'auto'}>
        <DeviceList selectedDevice={device} setSelectedDevice={setDevice} />
      </Grid>
      <Grid size={'grow'}>
        <DevicePanel device={device} />
      </Grid>
    </Grid>
  );
}
