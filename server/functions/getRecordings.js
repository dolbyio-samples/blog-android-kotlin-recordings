const axios = require('axios');
require('dotenv').config()

const SITE_URL = process.env.URL; // netlify provides this.
const APP_IDENTIFIER = process.env.APP_IDENTIFIER;
const CONSUMER_KEY = process.env.CONSUMER_KEY;
const CONSUMER_SECRET = process.env.CONSUMER_SECRET;
const credentials = new Buffer.from(
  CONSUMER_KEY + ':' + CONSUMER_SECRET).toString('base64');

const config = {
  headers: {
    Authorization: 'Basic ' + credentials,
  },
};

const data = { grant_type: 'client_credentials', expires_in: 3600 };

async function fetchToken() {
  try {
    let bearerAuthUrl = 'https://api.voxeet.com/v1/auth/token';
    const response = await axios.post(bearerAuthUrl, data, config);
    const { access_token, refresh_token, expires_in } = response.data;
    return {
      statusCode: 200,
      headers: {
        'Access-Control-Allow-Headers': 'Content-Type',
        'Access-Control-Allow-Origin': '*', // NOTE this is to allow for CORS when testing locally
        'Access-Control-Allow-Methods': 'OPTIONS,POST,GET',
      },
      body: access_token,
    };
  } catch (error) {
    return {
      statusCode: 200,
      body: JSON.stringify({ error: 'Unexpected error' }),
    };
  }
}

async function getRecordings(url, options) {
  var promise = new Promise(function (resolve, reject) {
    try {
      axios.get(url, options)
        .then((response) => {
          let recordings = response.data.recordings.map(item => {
            let confId = item["confId"]
            let duration = item["duration"] ? item["duration"] : "10000"
            let alias = item["alias"]
            return { confId, duration, alias }
          });
          resolve(recordings);
        })
        .catch((error) => {
          reject(error)
        });
    } catch (error) {
      reject(error);
    }
  });
  return promise;
}

exports.handler = async function (event, context) {
  let isValid = false;
  // Only allow POST
  if (event.httpMethod !== "POST") {
    isValid = false;
  } else {
    isValid = true;
  }

  // restrict to allow only from same domain host url
  if (APP_IDENTIFIER.toLowerCase() === "web") {
    if (event.headers.origin !== SITE_URL) {
      isValid = false;
    } else {
      isValid = true;
      return sendResonse(isValid);
    }
  }


  // restrict to allow only from same domain host url
  if (event.headers.appidentifier !== APP_IDENTIFIER) {
    isValid = false;
  } else {
    isValid = true;
    return sendResonse(isValid);
  }

  return sendResonse(false);

  async function sendResonse(isValid) {
    if (isValid == true) {
      try {
        let bearerToken = await fetchToken();
        let url = "https://api.voxeet.com/v1/monitor/recordings"
        var options = {
          method: 'GET',
          headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
            Authorization: `Bearer ${bearerToken.body}`
          }
        };
        let result = await getRecordings(url, options)
        let payload = {
          statusCode: 200,
          body: JSON.stringify(result)
        }
        return payload
      } catch (error) {
        return error
      }
    } else {
      return { statusCode: 405, body: "Method Not Allowed" };
    }
  }

} 
