import type { Route } from './+types/home';
import React, { useEffect } from 'react';
import { fetchTranslations } from '~/services/api.service';
import type { Translation } from '~/model/api.model';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import Box from '@mui/material/Box';
import { TextField, ToggleButton, ToggleButtonGroup } from '@mui/material';

export function meta({}: Route.MetaArgs) {
  return [{ title: 'BioWeather Dashboard - Translations' }];
}

const columns: GridColDef<Translation[][number]>[] = [
  { field: 'id', headerName: 'ID', width: 200 },
  {
    field: 'text',
    headerName: 'Text',
    width: 1000,
    editable: true,
  },
];

export default function Translations() {
  const [serverTranslations, setServerTranslations] = React.useState<Translation[]>([]);
  const [filteredTranslations, setFilteredTranslations] = React.useState<Translation[]>([]);
  const [languages, setLanguages] = React.useState<string[]>([]);
  const [currentLanguage, setCurrentLanguage] = React.useState<string | null>(null);
  const [query, setQuery] = React.useState<string | null>(null);
  const [loading, setLoading] = React.useState<boolean>(true);

  function filterTranslations(query: string | null, language: string | null) {
    setQuery(query);
    setCurrentLanguage(language);

    setFilteredTranslations(
      serverTranslations.filter((translation) => {
        const matchesQuery =
          !query ||
          translation?.text?.toLowerCase().includes(query.toLowerCase()) ||
          translation?.id?.toLowerCase().includes(query.toLowerCase());
        const matchesLanguage =
          !language || language === 'ALL' || translation.language === language;
        return matchesQuery && matchesLanguage;
      })
    );
  }

  async function loadTranslations() {
    try {
      setLoading(true);
      const res = await fetchTranslations();
      setServerTranslations(res);
      setLanguages([...new Set(res.map((t) => t.language))]);
      setFilteredTranslations(res);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadTranslations();
  }, []);

  return (
    <Box>
      <TextField
        size={'small'}
        fullWidth
        label="Filter"
        variant={'outlined'}
        onChange={(e) => filterTranslations(e.target.value || null, currentLanguage)}
      />

      <ToggleButtonGroup
        sx={{ paddingTop: 2 }}
        value={currentLanguage}
        onChange={(_event, value) => {
          setCurrentLanguage(value);
          filterTranslations(query, value === 'ALL' ? null : value);
        }}
        exclusive
      >
        <ToggleButton value={'ALL'}>ALL</ToggleButton>
        {languages.map((lang) => (
          <ToggleButton value={lang}>{lang}</ToggleButton>
        ))}
      </ToggleButtonGroup>

      <DataGrid
        loading={loading}
        rows={filteredTranslations}
        columns={columns}
        initialState={{
          pagination: {
            paginationModel: {
              pageSize: 10,
            },
          },
        }}
        pageSizeOptions={[5, 10]}
        sx={{ border: 0 }}
      />
    </Box>
  );
}
