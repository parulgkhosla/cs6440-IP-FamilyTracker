// const BASE_URL = "http://127.0.0.1:8086/api";

const BASE_URL = "https://pkhosla3-cs6440-trackerbackend.herokuapp.com/api";

export const loadPatientInfoById = ({
  patientId,
  setData,
  endpoint = "patients",
  setError = null,
  setLoading = null,
}) => {
  if (setLoading) {
    setLoading(true);
  }

  fetch(`${BASE_URL}/${endpoint}/${patientId}`,{mode: 'cors'})
    .then((res) => res.json())
    .then(
      (result) => {
          // alert(result)
        setData(result);
        if (setLoading) {
          setLoading(false);
        }
      },
      (error) => {
          alert(error)
        if (setError) {
          setError(error);
        }
        if (setLoading) {
          setLoading(false);
        }
      }
    );
};