import { useState, useEffect } from "react";

interface UseDebounceProps {
  value: string;
  delay: number;
}

function useDebounce({ value, delay }: UseDebounceProps) {
  const [debouncedValue, setDebouncedValue] = useState(value);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value);
      setIsLoading(false);
    }, delay);

    return () => {
      clearTimeout(handler);
      setIsLoading(false);
    };
  }, [value, delay]);

  return { debouncedValue, isLoading };
}

export default useDebounce;
