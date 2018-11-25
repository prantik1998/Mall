import rncryptor

# class secure():

#     def __init__(self):



data = '...'
password = '...'

# rncryptor.RNCryptor's methods
cryptor = rncryptor.RNCryptor()
encrypted_data = cryptor.encrypt(data, password)
decrypted_data = cryptor.decrypt(encrypted_data, password)
print(data, decrypted_data, encrypted_data)
encrypted_data = cryptor.encrypt(data, password)
decrypted_data = cryptor.decrypt(encrypted_data, password)
print(data, decrypted_data, encrypted_data)
# assert data == decrypted_data

