import React, { useEffect, useState } from 'react';
import { getConfig, updateConfig } from '~/services/api.service';
import type { Config } from '~/model/api.model';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import { Alert, Typography } from '@mui/material';
import cronstrue from 'cronstrue';
import Divider from '@mui/material/Divider';

export default function Config() {
  const [config, setConfig] = useState<Config | null>(null);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getConfig().then(setConfig);
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!config) return;
    setConfig({ ...config, [e.target.name]: e.target.value });
  };

  const handleNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!config) return;
    setConfig({ ...config, [e.target.name]: Number(e.target.value) });
  };

  const handleSave = async () => {
    if (!config) return;
    setSaving(true);
    setError(null);
    try {
      await updateConfig(config);
    } catch (e: any) {
      setError(e?.message || 'Failed to save configuration');
    }
    setSaving(false);
  };

  if (!config) return <div>Loading...</div>;

  let cronDescription = '';
  try {
    cronDescription = cronstrue.toString(config.notificationJobCron || '');
  } catch {
    cronDescription = 'Invalid cron expression';
  }

  return (
    <Box component="form" sx={{ maxWidth: 400, mx: 'auto', mt: 4 }}>
      <Stack spacing={2}>
        <TextField
          label="Notification Job Cron"
          name="notificationJobCron"
          value={config.notificationJobCron}
          onChange={handleChange}
          fullWidth
        />
        <Typography variant="caption" color="text.secondary">
          {cronDescription}
        </Typography>
        <Divider />
        <Typography variant="subtitle2">BW Notification Threshold (Minutes)</Typography>
        <TextField
          label="BW Min"
          name="bwNotificationThresholdMinMinutes"
          type="number"
          value={config.bwNotificationThresholdMinMinutes}
          onChange={handleNumberChange}
          fullWidth
        />
        <TextField
          label="BW Max"
          name="bwNotificationThresholdMaxMinutes"
          type="number"
          value={config.bwNotificationThresholdMaxMinutes}
          onChange={handleNumberChange}
          fullWidth
        />
        <Typography variant="subtitle2">CR Notification Threshold (Minutes)</Typography>
        <TextField
          label="CR Min"
          name="crNotificationThresholdMinMinutes"
          type="number"
          value={config.crNotificationThresholdMinMinutes}
          onChange={handleNumberChange}
          fullWidth
        />
        <TextField
          label="CR Max"
          name="crNotificationThresholdMaxMinutes"
          type="number"
          value={config.crNotificationThresholdMaxMinutes}
          onChange={handleNumberChange}
          fullWidth
        />
        <Divider />
        <TextField
          label="BW Today Notification Hour"
          name="bwTodayNotificationHour"
          value={config.bwTodayNotificationHour}
          onChange={handleChange}
          fullWidth
        />
        <TextField
          label="BW Tomorrow Notification Hour"
          name="bwTomorrowNotificationHour"
          value={config.bwTomorrowNotificationHour}
          onChange={handleChange}
          fullWidth
        />
        {error && <Alert severity={'error'}> {error}</Alert>}
        <Button variant="contained" onClick={handleSave} disabled={saving}>
          {saving ? 'Saving...' : 'Save'}
        </Button>
      </Stack>
    </Box>
  );
}
