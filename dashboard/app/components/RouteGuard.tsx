import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router';
import { getAuthHeader } from '~/services/api.service';

export function RouteGuard() {
  let navigate = useNavigate();
  let location = useLocation();

  useEffect(() => {
    const value = getAuthHeader();
    if (!value) {
      navigate('/login');
    }
  }, [location]);

  return null;
}
