# FIT3077 Assignment 2
# Machine Learning bonus mark question

# written by 
# Megan Ooi Jie Yi (30101670)
# Hew Ye Zea

# The purpose of this script is to poll the server to get data regarding patients with cholesterol reading. 
# The script gets data for every 50 pages and sleeps for 40 seconds to not overload the server. 
# The data is then put into a dataframe and written into a csv file for the machine learning task. 

# import required libraries
import requests
import pandas as pd
import time
from datetime import datetime

# server base url 
base_url = 'https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/'

# initialise different urls that'll be used
patients_url = base_url + "Patient/"
dReport_url = base_url +"DiagnosticReport"
observation_url = base_url + "Observation"

# get patients with total cholesterol values 
# reference: Assignment2FHIR.ipynb from FIT3077
def cholesterol_patients():

    # create data frame to store data
    patient_data = pd.DataFrame(columns=['id', 'Cholesterol', 'Age', 'BMI', 'Blood_Pressure', 'Triglycerides', 'LD_Lipoprotein', 'HD_Lipoprotein'])

    next_page = True
    next_url = observation_url
    count_page = 0
    count_page = 0

    # try to get 10k data sets
    while next_page and count_page <= 10000:
        oReports = requests.get(url = next_url + "?code=2093-3").json()

        next_page = False
        try:
            links = oReports['link']
        except: 
            print(oReports) # which url doesn't have a link?
            break
        for i in range(len(links)):
            link=links[i]
            if link['relation'] == 'next':
                next_page = True
                next_url = link['url']
                count_page += 1

        # extract data
        entry = oReports['entry']
        
        patient_arr = [] # use an array to store patient data. data added must follow data frame format            
        bmi, blood_pressure, triglycerides, ldlc, hdlc = None, None, None, None, None # initialisation
        
        cholesterol = entry[i]['resource']['valueQuantity']['value']
        patient_id = entry[i]['resource']['subject']['reference'][len('Patient/'):]

        # patient's basic information
        patientReports = requests.get(url = patients_url + patient_id).json()
        birthDate = datetime.strptime(patientReports['birthDate'], '%Y-%m-%d').date()

        # get patient age
        try:
            # check if patient is deceased
            deceasedDate = datetime.strptime(patientReports['deceasedDateTime'][:9], '%Y-%m-%d').date()
            patient_age = deceasedDate.year - birthDate.year
        except:
            # not deceased. catch error and get age this year
            patient_age = datetime.now().year - birthDate.year

        # go through all observations for the patient
        observations = requests.get(url = observation_url + "?patient=" + patient_id).json()
        entry = observations['entry']

        for o in range(len(entry)):
            o_resource = entry[o]['resource']
            # bmi reading
            if o_resource['code']['coding'][0]['code'] == "39156-5":
                bmi = o_resource['valueQuantity']['value']

            # blood pressure
            elif o_resource['code']['coding'][0]['code'] == "55284-4":
                blood_pressure = o_resource['component'][0]['valueQuantity']['value']

            # triglycerides
            elif o_resource['code']['coding'][0]['code'] == "2571-8":
                triglycerides = o_resource['valueQuantity']['value']

            # Low Density Lipoprotein Cholesterol
            elif o_resource['code']['coding'][0]['code'] == "18262-6":
                ldlc = o_resource['valueQuantity']['value']

            # High Density Lipoprotein Cholesterol
            elif o_resource['code']['coding'][0]['code'] == "2085-9":
                hdlc = o_resource['valueQuantity']['value']

        # store all information into an array
        patient_arr.append(patient_id)
        patient_arr.append(cholesterol)
        patient_arr.append(patient_age)
        patient_arr.append(bmi)
        patient_arr.append(blood_pressure)
        patient_arr.append(triglycerides)
        patient_arr.append(ldlc)
        patient_arr.append(hdlc)

        patient_data.loc[patient_id] = patient_arr # store into data frame

        # every 50 pages, append to csv file and sleep
        if count_page % 50 == 0:
            patient_data.to_csv('patient_data.csv', mode='a', header=False)
            patient_data = pd.DataFrame(columns=['id', 'Cholesterol', 'Age', 'BMI', 'Blood_Pressure', 'Triglycerides', 'LD_Lipoprotein', 'HD_Lipoprotein'])
            time.sleep(40) # sleep for 40s
    
    return patient_data

def write_to():
    # function is responsible of writing remaining elements in dataframe to csv file
    df = cholesterol_patients()
    df.to_csv('patient_data.csv', mode='a', header=False)

if __name__ == "__main__":
    write_to()
