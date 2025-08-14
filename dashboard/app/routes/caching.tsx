import type { Route } from './+types/home';
import type { CacheStatistics } from '~/model/api.model';
import React, { useEffect, useState } from 'react';
import { getCacheStatistics } from '~/services/api.service';
import Box from '@mui/material/Box';
import { Card, CardContent, CardHeader, Grid } from '@mui/material';
import Typography from '@mui/material/Typography';

export function meta({}: Route.MetaArgs) {
  return [{ title: 'BioWeather Dashboard - Caching' }];
}

export default function Caching() {
  const [cacheStatistics, setCacheStatistics] = useState<CacheStatistics | null>(null);

  useEffect(() => {
    getCacheStatistics().then((res) => setCacheStatistics(res));
  }, []);

  return (
    <Box>
      <Typography variant={'h4'} sx={{ marginBottom: 2 }}>
        Caches
      </Typography>

      <Grid container spacing={2} columns={12} wrap="wrap">
        {cacheStatistics &&
          Object.entries(cacheStatistics).map(([k, cacheStats]) => (
            <Card sx={{ minWidth: 280 }} key={k}>
              <CardHeader title={k}></CardHeader>
              <CardContent>
                {Object.entries(cacheStats).map(([statKey, statValue]) => (
                  <Grid container spacing={1} columns={12} key={statKey}>
                    <Grid size={'grow'}>
                      <Typography fontWeight="bold" color="primary">
                        {statKey
                          .replace(/([A-Z])/g, ' $1')
                          .replace(/^./, (str) => str.toUpperCase())}
                        :
                      </Typography>
                    </Grid>
                    <Grid size={4}>{String(statValue)}</Grid>
                  </Grid>
                ))}
              </CardContent>
            </Card>
          ))}
      </Grid>
    </Box>
  );
}
