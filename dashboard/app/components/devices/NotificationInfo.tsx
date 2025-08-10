import Box from '@mui/material/Box';
import { Grid, Paper, TextField } from '@mui/material';
import * as React from 'react';
import type { Device } from '~/model/api.model';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import Button from '@mui/material/Button';
import { useSnackbar } from 'notistack';
import { sendTestNotification } from '~/services/api.service';

// DeviceList.tsx
type NotificationsInfoProps = {
  device: Device;
};

export function NotificationsInfo({ device }: NotificationsInfoProps) {
  const [notificationText, setNotificationText] = React.useState<string>('');
  const { enqueueSnackbar } = useSnackbar();

  async function sentTestNotification(e?: React.FormEvent) {
    if (e) e.preventDefault();

    if (!notificationText) {
      return;
    }

    try {
      await sendTestNotification(device.pushToken, notificationText);
      enqueueSnackbar('Test notification sent!', {
        variant: 'success',
        autoHideDuration: 3000,
        anchorOrigin: {
          vertical: 'bottom',
          horizontal: 'center',
        },
      });
      setNotificationText('');
    } catch (err) {
      enqueueSnackbar('Failed to send test notification!', {
        variant: 'error',
        autoHideDuration: 3000,
        anchorOrigin: {
          vertical: 'bottom',
          horizontal: 'center',
        },
      });
    }
  }

  return (
    <Box>
      <Paper elevation={3} sx={{ padding: 2 }}>
        <Typography variant="h4">Notifications</Typography>
        <Divider sx={{ pt: 1, mb: 2 }} />

        <form>
          <Grid
            container
            columns={12}
            spacing={3}
            direction="row"
            sx={{
              justifyContent: 'center',
              alignItems: 'center',
            }}
          >
            <Grid size={'auto'}>
              <Typography variant={'h5'} fontWeight={'bold'} color={'primary'} align={'right'}>
                Test:
              </Typography>
            </Grid>
            <Grid size={'grow'}>
              <TextField
                size={'small'}
                fullWidth
                label="Notification Text"
                variant={'outlined'}
                value={notificationText}
                onChange={(e) => setNotificationText(e.target.value)}
              />
            </Grid>
            <Grid size={'auto'}>
              <Button
                sx={{ width: 200 }}
                variant={'contained'}
                type="submit"
                onClick={sentTestNotification}
              >
                Send
              </Button>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Box>
  );
}
