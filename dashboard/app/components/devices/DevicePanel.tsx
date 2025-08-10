import { Alert } from '@mui/material';
import * as React from 'react';
import type { Device } from '~/model/api.model';
import Box from '@mui/material/Box';
import { DeviceInfo } from '~/components/devices/DeviceInfo';
import { NotificationsInfo } from '~/components/notifications/NotificationInfo';

// DeviceList.tsx
type DeviceInfoProps = {
  device: Device | null;
};

export function DevicePanel({ device }: DeviceInfoProps) {
  return (
    <Box padding={1}>
      {!device && <Alert severity={'warning'}>No device selected!</Alert>}
      <Box gap={1} display={'flex'} flexDirection={'column'}>
        {device && <DeviceInfo device={device} />}
        {device && <NotificationsInfo device={device} />}
      </Box>
    </Box>
  );
}
