export default function useFetch(queryHook, params) {
  const { data, isLoading, error } = queryHook(params);

  return { data: data?.data?.data, isLoading, error };
}
