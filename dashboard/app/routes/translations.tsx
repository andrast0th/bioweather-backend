import type { Route } from './+types/translations';
import React, { useEffect } from 'react';
import {
  deleteTranslations,
  getLanguages,
  getTranslations,
  importTranslations,
} from '~/services/translation.service';
import type { Language, Translation } from '~/model/api.model';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import Box from '@mui/material/Box';
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  ToggleButton,
  ToggleButtonGroup,
  Typography,
} from '@mui/material';
import Button from '@mui/material/Button';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import Divider from '@mui/material/Divider';
import TextField from '@mui/material/TextField';
import { useSnackbar } from 'notistack';

export function meta({}: Route.MetaArgs) {
  return [{ title: 'BioWeather Dashboard - Translations' }];
}

const columns: GridColDef<Translation[][number]>[] = [
  { field: 'id', headerName: 'ID', width: 300 },
  {
    field: 'text',
    headerName: 'Text',
    flex: 1,
    editable: false,
  },
];

export default function Translations() {
  const [languages, setLanguages] = React.useState<Language[]>([]);
  const [currentLanguage, setCurrentLanguage] = React.useState<Language | null>(null);
  const { enqueueSnackbar } = useSnackbar();

  const [translations, setTranslations] = React.useState<Translation[]>([]);

  const [deleteOpen, setDeleteOpen] = React.useState(false);

  const handleDelete = async () => {
    if (currentLanguage) {
      await deleteTranslations(currentLanguage.code);
    }
    setDeleteOpen(false);

    await loadLanguages();
  };

  const [importOpen, setImportOpen] = React.useState(false);
  const [importLang, setImportLang] = React.useState('');
  const [importContent, setImportContent] = React.useState<any | null>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const file = e.target.files[0];

      const reader = new FileReader();
      reader.onload = (event) => {
        try {
          const fileContent = event.target?.result as string;
          const json = JSON.parse(fileContent);
          setImportContent(json);
        } catch (err) {
          enqueueSnackbar('Invalid JSON file!', {
            variant: 'error',
            autoHideDuration: 3000,
            anchorOrigin: {
              vertical: 'bottom',
              horizontal: 'center',
            },
          });
        }
      };

      reader.readAsText(file);
    }
  };

  const handleImport = async () => {
    if (!importLang || !importContent) return;

    await importTranslations(importLang, importContent);
    setImportOpen(false);

    enqueueSnackbar('Import complete!', {
      variant: 'success',
      autoHideDuration: 3000,
      anchorOrigin: {
        vertical: 'bottom',
        horizontal: 'center',
      },
    });

    loadLanguages();
  };

  async function loadLanguages() {
    const lang = await getLanguages();
    setLanguages(lang);
    setCurrentLanguage(lang[0]);
  }

  useEffect(() => {
    setLoading(true);
    loadLanguages().finally(() => setLoading(false));
  }, []);

  const [loading, setLoading] = React.useState<boolean>(true);

  useEffect(() => {
    if (currentLanguage) {
      setLoading(true);
      getTranslations(currentLanguage.code)
        .then((res) => setTranslations(res))
        .finally(() => setLoading(false));
    }
  }, [currentLanguage]);

  return (
    <Box>
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          marginBottom: 2,
        }}
      >
        <ToggleButtonGroup
          value={currentLanguage}
          onChange={(_event, value) => {
            setCurrentLanguage(value);
          }}
          exclusive
        >
          {languages.map((lang) => (
            <ToggleButton key={lang.code} value={lang}>
              <Typography>
                {lang.name}({lang.code})
              </Typography>
            </ToggleButton>
          ))}
        </ToggleButtonGroup>

        <Box sx={{ display: 'flex', gap: 2 }}>
          <Divider orientation={'vertical'} flexItem></Divider>
          <Button
            disabled={!currentLanguage || currentLanguage?.code === 'en'}
            color="error"
            variant="contained"
            onClick={() => setDeleteOpen(true)}
            sx={{ fontWeight: 'bold' }}
          >
            Delete
          </Button>
          <Button
            onClick={() => {
              window.open(`/api/v1/translations/${currentLanguage?.code}/export`, '_blank');
            }}
            startIcon={<FileDownloadIcon />}
          >
            Export
          </Button>
          <Button startIcon={<FileUploadIcon />} onClick={() => setImportOpen(true)}>
            Import
          </Button>
        </Box>
      </Box>

      <Dialog open={deleteOpen} onClose={() => setDeleteOpen(false)}>
        <DialogTitle>Confirm Delete</DialogTitle>
        <DialogContent>
          <Typography color="error">Are you sure you want to delete this language?</Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteOpen(false)}>Cancel</Button>
          <Button onClick={handleDelete} color="error" variant="contained" autoFocus>
            Delete
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={importOpen} onClose={() => setImportOpen(false)}>
        <DialogTitle>Import language</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, minWidth: 350 }}>
          <TextField
            variant={'standard'}
            label="Language code"
            value={importLang}
            onChange={(e) => setImportLang(e.target.value)}
          />
          <input
            type="file"
            accept=".json"
            style={{ display: 'none' }}
            id="import-file-input"
            onChange={handleFileChange}
          />
          <label htmlFor="import-file-input">
            <Button variant="outlined" component="span">
              {importContent
                ? `${countLeafKeys(importContent)} keys to be imported`
                : 'Select file'}
            </Button>
          </label>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setImportOpen(false)}>Cancel</Button>
          <Button
            onClick={handleImport}
            color="success"
            variant="contained"
            autoFocus
            disabled={!importLang || !importContent}
          >
            Import
          </Button>
        </DialogActions>
      </Dialog>

      <DataGrid
        loading={loading}
        rows={translations}
        columns={columns}
        initialState={{
          pagination: {
            paginationModel: {
              pageSize: 50,
            },
          },
        }}
        pageSizeOptions={[5, 10, 50, 100]}
        sx={{ border: 0 }}
      />
    </Box>
  );
}

function countLeafKeys(obj: any): number {
  if (obj === null || typeof obj !== 'object') return 0;
  let count = 0;
  for (const key in obj) {
    const value = obj[key];
    if (value !== null && typeof value === 'object' && !Array.isArray(value)) {
      count += countLeafKeys(value);
    } else {
      count += 1;
    }
  }
  return count;
}
