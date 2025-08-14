import * as React from 'react';
import { useEffect } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { getActuatorInfo } from '~/services/api.service';
import { dateIsoToString } from '~/services/util.service';

export default function AppBarFooter() {
  const [info, setInfo] = React.useState<String>('');

  useEffect(() => {
    getActuatorInfo().then((info) => {
      setInfo(
        `${info?.build.version} | Git: ${info?.git.commit.id} | Built: ${dateIsoToString(info.build.time)}`
      );
    });
  }, []);

  return (
    <Box
      sx={{
        position: 'fixed',
        bottom: 0,
        left: 0,
        width: '100%',
      }}
    >
      <AppBar
        position="relative"
        sx={{ paddingLeft: 3, paddingRight: 3, paddingTop: 1, paddingBottom: 1 }}
      >
        {info && <Typography textAlign={'center'}>{info}</Typography>}
      </AppBar>
    </Box>
  );
}
