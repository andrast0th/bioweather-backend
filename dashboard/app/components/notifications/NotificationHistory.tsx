import Box from '@mui/material/Box';
import * as React from 'react';
import { useEffect } from 'react';
import type { Device, PushTicket, Subscription } from '~/model/api.model';
import Typography from '@mui/material/Typography';
import { fetchNotificationHistory, fetchSubscriptions } from '~/services/api.service';
import { CircularProgress, Paper, ToggleButton, ToggleButtonGroup } from '@mui/material';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import { dateIsoToString } from '~/services/util.service';

const columns: GridColDef<PushTicket[][number]>[] = [
  { field: 'id', headerName: 'Push Ticket ID', width: 200 },
  {
    field: 'locationId',
    headerName: 'Location ID',
    width: 150,
    valueGetter: (value, row) => value ?? '-',
  },
  {
    field: 'notificationType',
    headerName: 'Type',
    width: 100,
  },
  {
    field: 'ticketCreatedAt',
    headerName: 'Created',
    width: 200,
    valueGetter: (value, row) => dateIsoToString(value),
  },
  {
    field: 'receiptCheckedAt',
    headerName: 'Checked',
    width: 200,
    valueGetter: (value, row) => (row.wasReceiptChecked ? dateIsoToString(value) : '-'),
  },
  {
    field: 'notificationTitle',
    headerName: 'Notification Title',
    width: 150,
  },
  {
    field: 'notificationBody',
    headerName: 'Notification Content',
    width: 150,
  },
  {
    field: 'notificationBody',
    headerName: 'Notification Content',
    width: 150,
  },
];

// DeviceList.tsx
type NotificationHistoryProps = {
  device: Device;
};

export function NotificationHistory({ device }: NotificationHistoryProps) {
  const [notificationHistory, setNotificationHistory] = React.useState<PushTicket[]>([]);
  const [subscriptions, setSubscriptions] = React.useState<Subscription[]>([]);
  const [filterLocationId, setFilterLocationId] = React.useState(() => 'ALL');
  const [selectedSub, setSelectedSub] = React.useState<Subscription | null>(null);
  const [loading, setLoading] = React.useState<boolean>(false);

  async function loadData() {
    setLoading(true);

    setSubscriptions(await fetchSubscriptions(device.pushToken));
    const locationId = filterLocationId === 'ALL' ? null : filterLocationId;
    setNotificationHistory(await fetchNotificationHistory(device.pushToken, locationId));

    setLoading(false);
  }

  useEffect(() => {
    loadData();
  }, [device.pushToken, filterLocationId]);

  useEffect(() => {
    setLoading(true);

    try {
      const locationId = filterLocationId === 'ALL' ? null : filterLocationId;
      setSelectedSub(subscriptions.find((sub) => sub.locationId === locationId) || null);
      fetchNotificationHistory(device.pushToken, locationId).then((res) =>
        setNotificationHistory(res)
      );
    } finally {
      setLoading(false);
    }
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
            <ToggleButton value={subscription.locationId}>{subscription.locationName}</ToggleButton>
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

      {loading && (
        <Box display={'flex'} justifyContent="center" alignItems="center" height={100}>
          <CircularProgress />
        </Box>
      )}

      <Paper sx={{ height: 400, width: '100%', mt: 2 }}>
        <DataGrid
          loading={loading}
          rows={notificationHistory}
          columns={columns}
          initialState={{
            pagination: {
              paginationModel: {
                pageSize: 5,
              },
            },
          }}
          pageSizeOptions={[5, 10]}
          sx={{ border: 0 }}
        />
      </Paper>
    </Box>
  );
}
