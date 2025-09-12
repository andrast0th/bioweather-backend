import Box from '@mui/material/Box';
import * as React from 'react';
import { useEffect } from 'react';
import type { Device, NotificationInfo, PushTicket, Subscription } from '~/model/api.model';
import Typography from '@mui/material/Typography';
import {
  fetchNotificationHistory,
  fetchSubscriptions,
  reSendNotification,
} from '~/services/api.service';
import { CircularProgress, Paper, ToggleButton, ToggleButtonGroup } from '@mui/material';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import Button from '@mui/material/Button';

const columns: GridColDef<PushTicket[][number]>[] = [
  {
    field: 'notificationType',
    headerName: 'Type',
    width: 100,
  },
  {
    field: 'ticketCreatedAt',
    headerName: 'Created',
    width: 200,
    valueGetter: (value, row) => new Date(value).toISOString(),
  },
  {
    field: 'receiptCheckedAt',
    headerName: 'Checked',
    width: 100,
    valueGetter: (value, row) => (row.wasReceiptChecked ? new Date(value).toISOString() : '-'),
  },
  {
    field: 'notificationTitle',
    headerName: 'Notification Title',
    width: 150,
  },
  {
    field: 'notificationBody',
    headerName: 'Notification Content',
    flex: 1,
  },
  {
    field: 'actions',
    headerName: 'Actions',
    width: 120,
    sortable: false,
    filterable: false,
    renderCell: (params) => (
      <Button
        variant="contained"
        size="small"
        onClick={async () => {
          await reSendNotification(params.row.id);
        }}
      >
        Re-Send
      </Button>
    ),
  },
];

// DeviceList.tsx
type NotificationHistoryProps = {
  device: Device;
};

export function NotificationHistory({ device }: NotificationHistoryProps) {
  const [notificationHistory, setNotificationHistory] = React.useState<PushTicket[]>([]);

  const [subscriptions, setSubscriptions] = React.useState<Subscription | null>(null);
  const [selectedNotificationInfo, setSelectedNotificationInfo] =
    React.useState<NotificationInfo | null>(null);

  const [filterLocationId, setFilterLocationId] = React.useState(() => 'ALL');
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

      setSelectedNotificationInfo(
        subscriptions?.notificationInfo.find((ni) => ni.locationId === locationId) || null
      );

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
          {subscriptions?.notificationInfo.map((ni) => (
            <ToggleButton value={ni.locationId}>{ni.locationName}</ToggleButton>
          ))}
        </ToggleButtonGroup>
      </Box>

      {selectedNotificationInfo && selectedNotificationInfo.locationId && (
        <Box display={'flex'} flexDirection={'row'} gap={2} alignItems={'center'} marginTop={1}>
          <Typography fontWeight={'bold'} color={'primary'}>
            Active notification types:
          </Typography>
          <Typography>{selectedNotificationInfo.notificationTypes.join(', ')}</Typography>
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
            sorting: {
              sortModel: [{ field: 'ticketCreatedAt', sort: 'desc' }],
            },
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
