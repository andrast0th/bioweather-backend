import Box from '@mui/material/Box';
import { Grid, Paper } from '@mui/material';
import * as React from 'react';
import type { Device } from '~/model/api.model';
import { dateIsoToString, getTimezoneName } from '~/services/util.service';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';

// DeviceList.tsx
type DeviceInfoProps = {
  device: Device;
};

export function DeviceInfo({ device }: DeviceInfoProps) {
  return (
    <Box>
      <Paper elevation={3} sx={{ padding: 2 }}>
        <Typography variant="h4">Device: {device.deviceInfo.deviceName}</Typography>
        <Divider sx={{ pt: 1, mb: 2 }} />
        <Grid container spacing={1} columns={12} wrap={'wrap'}>
          <Grid size={3}>
            <Typography fontWeight={'bold'} color={'primary'}>
              Push Token:
            </Typography>
          </Grid>
          <Grid size={9}>
            <Typography sx={{ wordBreak: 'break-word' }}>{device.pushToken}</Typography>
          </Grid>

          <Grid size={3}>
            <Typography fontWeight={'bold'} color={'primary'}>
              Device Language:
            </Typography>
          </Grid>
          <Grid size={9}>{device.language}</Grid>

          <Grid size={3}>
            <Typography fontWeight={'bold'} color={'primary'}>
              Device Timezone:
            </Typography>
          </Grid>
          <Grid size={9}>{getTimezoneName(device.timezoneOffset)}</Grid>

          <Grid size={3}>
            <span className={'font-bold'}>
              <Typography fontWeight={'bold'} color={'primary'}>
                Activity:
              </Typography>
            </span>
          </Grid>
          <Grid size={9}>{dateIsoToString(device.updatedTimestamp)}</Grid>

          <Grid size={3}>
            <Typography fontWeight={'bold'} color={'primary'}>
              Device:
            </Typography>
          </Grid>
          <Grid size={9}>
            {device.deviceInfo.brand} {device.deviceInfo.modelName} | OS: {device.deviceInfo.osName}{' '}
            {device.deviceInfo.osVersion}
          </Grid>
        </Grid>
      </Paper>
    </Box>
  );
}
