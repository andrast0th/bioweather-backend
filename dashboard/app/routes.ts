import { index, route, type RouteConfig } from '@react-router/dev/routes';

export default [
  index('routes/home.tsx'),
  route('login', 'routes/login.tsx'),
  route('caching', 'routes/caching.tsx'),
  route('config', 'routes/config.tsx'),
  route('translations', 'routes/translations.tsx'),
] satisfies RouteConfig;
