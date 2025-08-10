export function getTimezoneName(offsetMinutes: number): string {
  const date = new Date();
  // Offset in milliseconds
  const localDate = new Date(date.getTime() + offsetMinutes * 60000);
  return (
    Intl.DateTimeFormat('en-US', { timeZoneName: 'short' }).format(localDate).split(' ').pop() ?? ''
  );
}

export function dateIsoToString(dateIso: string): string {
  const date = new Date(dateIso);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  });
}
