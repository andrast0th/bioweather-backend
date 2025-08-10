import Box from '@mui/material/Box';
import * as React from 'react';
import { useEffect } from 'react';
import type { Device, Subscription } from '~/model/api.model';
import Typography from '@mui/material/Typography';
import { fetchSubscriptions } from '~/services/api.service';
import { ToggleButton, ToggleButtonGroup } from '@mui/material';

// DeviceList.tsx
type NotificationHistoryProps = {
  device: Device;
};

export function NotificationHistory({ device }: NotificationHistoryProps) {
  useEffect(() => {
    fetchSubscriptions(device.pushToken).then((res) => setSubscriptions(res));
  }, [device.pushToken]);

  const [subscriptions, setSubscriptions] = React.useState<Subscription[]>([]);
  const [filterLocationId, setFilterLocationId] = React.useState(() => 'ALL');
  const [selectedSub, setSelectedSub] = React.useState<Subscription | null>(null);

  useEffect(() => {
    const locationId = filterLocationId === 'ALL' ? null : filterLocationId;
    setSelectedSub(subscriptions.find((sub) => sub.locationId === locationId) || null);
  }, [filterLocationId]);

  const handleFormat = (_event: React.MouseEvent<HTMLElement>, filterLocationId: string) => {
    setFilterLocationId(filterLocationId);
  };

  return (
    <Box sx={{ mt: 2 }}>
      <Typography variant={'h5'}>History</Typography>

      <Box display={'flex'} flexDirection={'row'} gap={2} alignItems={'center'}>
        <Typography fontWeight={'bold'} color={'primary'}>
          Locations
        </Typography>
        <ToggleButtonGroup value={filterLocationId} onChange={handleFormat} exclusive>
          <ToggleButton value={'ALL'}>ALL</ToggleButton>
          {subscriptions.map((subscription) => (
            <ToggleButton value={subscription.locationId}>{subscription.locationId}</ToggleButton>
          ))}
        </ToggleButtonGroup>
      </Box>

      {selectedSub && selectedSub.locationId && (
        <Box display={'flex'} flexDirection={'row'} gap={2} alignItems={'center'} marginTop={1}>
          <Typography fontWeight={'bold'} color={'primary'}>
            Active notification types:
          </Typography>
          <Typography>{selectedSub.notificationTypes.join(', ')}</Typography>
        </Box>
      )}
    </Box>
  );
}
